package com.example.urlshortner.interfaces;

import com.example.urlshortner.model.UrlDao;

public interface UrlShortnerService {
    Integer addNewUrlToShorten(UrlDao urlDao);
}
