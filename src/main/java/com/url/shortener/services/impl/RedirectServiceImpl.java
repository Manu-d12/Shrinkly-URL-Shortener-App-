package com.url.shortener.services.impl;

import com.url.shortener.models.ClickEvent;
import com.url.shortener.models.UrlMapping;
import com.url.shortener.repositories.UrlMappingRepo;
import com.url.shortener.services.RedirectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class RedirectServiceImpl implements RedirectService {

    @Autowired
    private UrlMappingRepo urlMappingRepo;

    @Override
    public String getLongUrl(String shortUrl) {
        UrlMapping urlMapping = this.urlMappingRepo.findByShortUrl(shortUrl);
        if(urlMapping == null) return  null;
        String originalUrl = urlMapping.getOriginalUrl();
        urlMapping.setClickCount(urlMapping.getClickCount() + 1);
        ClickEvent clickEvent = ClickEvent.builder()
                .urlMapping(urlMapping)
                .clickDate(LocalDateTime.now())
                .build();
        urlMapping.getClickEvents().add(clickEvent);
        this.urlMappingRepo.save(urlMapping);
        return  originalUrl;
    }
}
