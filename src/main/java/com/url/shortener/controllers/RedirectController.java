package com.url.shortener.controllers;

import com.url.shortener.services.RedirectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/")
public class RedirectController {

    @Autowired
    private RedirectService redirectService;

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> redirectUrlHandler(
            @PathVariable String shortUrl
    ) {
        String longUrl = this.redirectService.getLongUrl(shortUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(longUrl));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_TEMPORARILY);
    }
}
