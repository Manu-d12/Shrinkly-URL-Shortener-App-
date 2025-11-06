package com.url.shortener.repositories;

import com.url.shortener.models.UrlMapping;
import com.url.shortener.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UrlMappingRepo extends JpaRepository<UrlMapping, String > {
    public UrlMapping findByUserAndOriginalUrl(User user, String originalUrl);
    public List<UrlMapping> findByUser(User user);
    public UrlMapping findByShortUrl(String shortUrl);
}
