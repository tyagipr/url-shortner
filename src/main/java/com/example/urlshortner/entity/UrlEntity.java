package com.example.urlshortner.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "URLS")
public class UrlEntity {
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "ORIGINAL_URL")
    private String originalUrl;

    @Column(name = "SHORT_URL")
    private String shortUrl;

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name = "USER_ID")
    private String userId;

    @Override
    public String toString() {
        return "UrlEntity{" +
                "id=" + id +
                ", originalUrl='" + originalUrl + '\'' +
                ", shortUrl='" + shortUrl + '\'' +
                ", userId=" + userId +
                '}';
    }
}
