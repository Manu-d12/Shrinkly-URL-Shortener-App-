package com.url.shortener.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    // get Jwt Token, extract user-name, check its expiry and then validate


        String token = this.jwtService.extractToken(request);
        logger.info("REQUEST_SOURCE: {}", request.getHeader("Origin"));
        logger.info("TOKEN: {}",token);
        String username = null;
        try {
            username = this.jwtService.extractUsername(token);
        } catch (IllegalArgumentException ex) {
            logger.info("IllegalArgumentException");
            ex.printStackTrace();
        } catch (ExpiredJwtException ex) {
            logger.info("ExpiredJwtException : Given JWT Token is expired");
            ex.printStackTrace();
        } catch (MalformedJwtException ex) {
            logger.info("MalformedJwtException: Given JWT Token Changed");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if(username != null && !this.jwtService.isTokenExpired(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
            // need validate the username......
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);


            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }
}
