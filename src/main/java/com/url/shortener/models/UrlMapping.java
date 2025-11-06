package com.url.shortener.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "url_mapping")
public class UrlMapping {
    @Id
    private String id;

    @Column(name = "original_url", nullable = false)
    private String originalUrl;

    @Column(name = "short_url", nullable = false)
    private String shortUrl;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "click_count")
    private int clickCount;

    @OneToMany(mappedBy = "urlMapping", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ClickEvent> clickEvents;
}
