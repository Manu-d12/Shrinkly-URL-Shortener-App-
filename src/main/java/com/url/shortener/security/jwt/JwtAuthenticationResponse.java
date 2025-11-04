package com.url.shortener.security.jwt;

import com.url.shortener.dtos.UserDto;
import lombok.*;

@Getter
@Setter
@Builder
public class JwtAuthenticationResponse {
    private String token;
    private UserDto user;
}
