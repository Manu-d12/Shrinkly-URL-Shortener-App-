package com.url.shortener.controllers;


import com.url.shortener.dtos.ClickEventDto;
import com.url.shortener.dtos.ShortUrlResponseDto;
import com.url.shortener.dtos.ShortenUrlRequestDto;
import com.url.shortener.dtos.UserDto;
import com.url.shortener.services.UrlMappingService;
import com.url.shortener.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/urls/")
public class UrlMappingController {

    @Autowired
    private UrlMappingService urlMappingService;

    @Autowired
    private UserService userService;


    @PreAuthorize("hasRole('USER')")
    @PostMapping("/shorten")
    public ResponseEntity<ShortUrlResponseDto> convertLongUrl(
            @Valid @RequestBody ShortenUrlRequestDto shortenUrlRequestDto,
            Principal principal
    ) {
        String username = principal.getName();
        UserDto user = this.userService.findByUsername(username);
        ShortUrlResponseDto shortUrlResponseDto = this.urlMappingService.convertLongUrl(shortenUrlRequestDto, user);
        return ResponseEntity.ok(shortUrlResponseDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/myurls")
    public ResponseEntity<List<ShortUrlResponseDto>> getUserShortUrls(Principal principal) {
        String username = principal.getName();
        UserDto userDto = this.userService.findByUsername(username);
        List<ShortUrlResponseDto> ll = this.urlMappingService.getShortUrlsByUser(userDto);
        return ResponseEntity.ok(ll);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/anayltics/{shortUrl}")
    public ResponseEntity<List<ClickEventDto>> getAnalytics(
            @PathVariable String shortUrl,
            @RequestParam String startDate,
            @RequestParam String endDate

    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);

        List<ClickEventDto> list = this.urlMappingService.getClickEventsByDate(shortUrl, start, end);
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/totalClicks")
    public ResponseEntity<Map<LocalDate, Long>> getTotalClicksByDate(
            Principal principal,
            @RequestParam String startDate,
            @RequestParam String endDate

    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);

        UserDto userDto = this.userService.findByUsername(principal.getName());

        Map<LocalDate, Long> totalClicksByDate = this.urlMappingService.getTotalClicksByDate(userDto, start, end);
        return ResponseEntity.ok(totalClicksByDate);
    }
}
