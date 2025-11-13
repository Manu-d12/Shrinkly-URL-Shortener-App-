package com.url.shortener.controllers;


import com.url.shortener.dtos.LoginRequest;
import com.url.shortener.dtos.UserDto;
import com.url.shortener.exceptions.ResourceNotFoundException;
import com.url.shortener.security.jwt.JWTService;
import com.url.shortener.security.jwt.JwtAuthenticationResponse;
import com.url.shortener.services.UserService;
import com.url.shortener.services.impl.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto) {
        UserDto user = this.userService.createUser(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
            Map<String, Object> claims = new HashMap<>();
            Collection<? extends GrantedAuthority> authorities = (List<? extends GrantedAuthority>) user.getAuthorities();
            claims.put("roles", authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList());

            String token = this.jwtService.generateToken(claims, loginRequest.getUsername());
            JwtAuthenticationResponse jwtAuthenticationResponse = JwtAuthenticationResponse.builder()
                    .token(token)
                    .user(UserDto.builder()
                            .username(user.getUsername())
                            .email(user.getEmail())
                            .id(user.getId())
                            .role(user.getRole())
                            .createdDate(user.getCreatedDate())
                            .build())
                    .build();
            return ResponseEntity.ok(jwtAuthenticationResponse);
        } catch (Exception ex) {
            System.out.println("HERE");
            throw new ResourceNotFoundException("USER_NOT_FOUND", HttpStatus.NOT_FOUND);
        }
    }
}
