package com.example.urlshortner.interfaces;

import com.example.urlshortner.entity.UrlEntity;
import com.example.urlshortner.model.UrlDao;
import com.example.urlshortner.model.UrlDto;

public interface UrlShortnerService {
    Integer addNewUrlToShorten(UrlDao urlDao);
    UrlDto getShortUrlById(Integer id);
}
