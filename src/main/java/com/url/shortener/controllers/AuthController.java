package com.url.shortener.controllers;


import com.url.shortener.dtos.LoginRequest;
import com.url.shortener.dtos.UserDto;
import com.url.shortener.exceptions.ResourceNotFoundException;
import com.url.shortener.security.jwt.JWTService;
import com.url.shortener.security.jwt.JwtAuthenticationResponse;
import com.url.shortener.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        if(authentication.isAuthenticated()) {
            // fetch user from DB and its role and all....
            // add username as subject in the JwT and roles in the claim...
            UserDto userDto = this.userService.findByUsername(loginRequest.getUsername());
            Map<String, Object> claims = new HashMap<>();
            claims.put("ROLE", userDto.getRole());
            String token = this.jwtService.generateToken(claims, loginRequest.getUsername());
            JwtAuthenticationResponse jwtAuthenticationResponse = JwtAuthenticationResponse.builder()
                    .token(token)
                    .user(userDto)
                    .build();
            return ResponseEntity.ok(jwtAuthenticationResponse);
        }
        throw new ResourceNotFoundException("user not found with given username and password", HttpStatus.NOT_FOUND);
    }
}
