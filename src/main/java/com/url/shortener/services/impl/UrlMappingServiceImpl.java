package com.url.shortener.services.impl;

import com.url.shortener.dtos.ClickEventDto;
import com.url.shortener.dtos.ShortUrlResponseDto;
import com.url.shortener.dtos.ShortenUrlRequestDto;
import com.url.shortener.dtos.UserDto;
import com.url.shortener.helper.Helper;
import com.url.shortener.helper.Snowflake;
import com.url.shortener.models.ClickEvent;
import com.url.shortener.models.UrlMapping;
import com.url.shortener.models.User;
import com.url.shortener.repositories.ClickEventRepo;
import com.url.shortener.repositories.UrlMappingRepo;
import com.url.shortener.services.UrlMappingService;
import org.apache.juli.logging.Log;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UrlMappingServiceImpl implements UrlMappingService {

    private Logger logger = LoggerFactory.getLogger(UrlMappingServiceImpl.class);

    @Autowired
    private UrlMappingRepo urlMappingRepo;

    @Autowired
    private ClickEventRepo clickEventRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private Snowflake uniqueIdGenerator;

    @Override
    public ShortUrlResponseDto convertLongUrl(ShortenUrlRequestDto shortenUrlRequestDto, UserDto userDto) {
        User user = this.modelMapper.map(userDto, User.class);
        UrlMapping urlMapping = this.urlMappingRepo.findByUserAndOriginalUrl(user, shortenUrlRequestDto.getOriginalUrl());
        if(urlMapping != null) {
           return this.modelMapper.map(urlMapping, ShortUrlResponseDto.class);
        }
        String urlMappingId = Helper.generateUUID();
        long uniqueId = uniqueIdGenerator.nextId();
        String shortUrl = Helper.generateShortUrl(uniqueId);
        UrlMapping urlMappingToSave = UrlMapping.builder()
                .id(urlMappingId)
                .originalUrl(shortenUrlRequestDto.getOriginalUrl())
                .shortUrl(shortUrl)
                .id(urlMappingId)
                .user(user)
                .clickCount(0)
                .createdDate(LocalDateTime.now())
                .build();
        UrlMapping savedUrlMapping = this.urlMappingRepo.save(urlMappingToSave);
        return this.modelMapper.map(savedUrlMapping, ShortUrlResponseDto.class);
    }

    @Override
    public List<ShortUrlResponseDto> getShortUrlsByUser(UserDto userDto) {
        User user = this.modelMapper.map(userDto, User.class);
        List<UrlMapping> userUrls = this.urlMappingRepo.findByUser(user);
        return userUrls.stream().map(mapping -> {
            return this.modelMapper.map(mapping, ShortUrlResponseDto.class);
        }).toList();
    }

    @Override
    public List<ClickEventDto> getClickEventsByDate(String shortUrl, LocalDateTime start, LocalDateTime end) {
        UrlMapping urlMapping = this.urlMappingRepo.findByShortUrl(shortUrl);

        logger.info("URL_MAPPING: {}", urlMapping);
        logger.info("SHORT_URL: {}", shortUrl);

        if (urlMapping == null) {
            return List.of();
        }
        List<ClickEvent> clickEvents = this.clickEventRepo.findByUrlMappingAndClickDateBetween(urlMapping, start, end);
        Map<LocalDate, Long> grouped = clickEvents.stream()
                .collect(Collectors.groupingBy(
                        click -> click.getClickDate().toLocalDate(),
                        Collectors.counting()
                ));
        return grouped.entrySet().stream()
                .map(entry -> {
                    ClickEventDto dto = new ClickEventDto();
                    dto.setDate(entry.getKey());
                    dto.setClickCount(entry.getValue());
                    return dto;
                })
                .toList();
    }

    @Override
    public Map<LocalDate, Long> getTotalClicksByDate(UserDto userDto, LocalDate start, LocalDate end) {
        User user = this.modelMapper.map(userDto, User.class);
        List<UrlMapping> mappings = this.urlMappingRepo.findByUser(user);

        if(mappings == null) return new HashMap<>();

        List<ClickEvent> clickEvents = this.clickEventRepo.findByUrlMappingInAndClickDateBetween(mappings, start.atStartOfDay(), end.plusDays(1).atStartOfDay());
        return clickEvents.stream()
                .collect(Collectors.groupingBy(
                        click -> click.getClickDate().toLocalDate(),
                        Collectors.counting()
                ));
    }


}

