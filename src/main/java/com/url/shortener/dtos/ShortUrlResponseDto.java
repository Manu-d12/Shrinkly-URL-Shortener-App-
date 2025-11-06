package com.url.shortener.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShortUrlResponseDto {
    private String shortUrl;
    private String originalUrl;
    private String id;
    private LocalDateTime createdDate;
    private int clickCount;
}
