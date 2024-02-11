package com.example.urlshortner.service;

import com.example.urlshortner.entity.UrlEntity;
import com.example.urlshortner.exception.CustomException;
import com.example.urlshortner.model.UrlDao;
import com.example.urlshortner.model.UrlDto;

import java.util.List;

public interface UrlShortnerServiceInterface {
    UrlDto addNewUrlToShorten(UrlDao urlDao) throws Exception;
    UrlDto getShortUrlById(Long id) throws Exception;


    void deleteUrlById(Long id) throws Exception;

    void modifyOriginalUrl(String updatedUrl, Long id) throws CustomException;

    List<UrlDto> getAllEntries();
    void deleteAllEntries();
}
