package com.example.urlshortner.service;

import com.example.urlshortner.entity.UrlEntity;
import com.example.urlshortner.model.UrlDao;
import com.example.urlshortner.model.UrlDto;

public interface UrlShortnerServiceInterface {
    UrlDto addNewUrlToShorten(UrlDao urlDao) throws Exception;
    UrlDto getShortUrlById(Long id) throws Exception;


    void deletedShortUrlById(Long id) throws Exception;

    void modifyOriginalUrl(UrlDao urlDao, Long id) throws Exception;
}
