package com.url.shortener.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@AllArgsConstructor
public class ShortenUrlRequestDto {
    @URL(message = "url should be valid")
    @NotBlank(message = "long url cannot be blank")
    private String originalUrl;
}
