package com.url.shortener.services;

import com.url.shortener.dtos.ClickEventDto;
import com.url.shortener.dtos.ShortUrlResponseDto;
import com.url.shortener.dtos.ShortenUrlRequestDto;
import com.url.shortener.dtos.UserDto;
import com.url.shortener.models.User;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface UrlMappingService {
    public ShortUrlResponseDto convertLongUrl(ShortenUrlRequestDto shortenUrlRequestDto, UserDto userDto);
    public List<ShortUrlResponseDto> getShortUrlsByUser(UserDto user);
    public List<ClickEventDto> getClickEventsByDate(String shortUrl, LocalDateTime start, LocalDateTime end);
    public Map<LocalDate, Long> getTotalClicksByDate(UserDto userDto, LocalDate start, LocalDate end);
}
