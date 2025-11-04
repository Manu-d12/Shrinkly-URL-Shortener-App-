package com.url.shortener.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

public class UserDto {

    private String id;

    @Email(message = "Email should be valid")
    private String email;

    @Size(min = 4, max = 20, message = "Username length should be >= 4 and <= 20")
    private String username;

    @Size(min = 6, max = 20, message = "Password length should be >= 4 and <= 20")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String role = "ROLE_NORMAL_USER";

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdDate;
}
