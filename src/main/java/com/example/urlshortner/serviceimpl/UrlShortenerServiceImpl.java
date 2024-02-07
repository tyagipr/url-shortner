package com.example.urlshortner.serviceimpl;

import com.example.urlshortner.entity.UrlEntity;
import com.example.urlshortner.interfaces.UrlShortnerService;
import com.example.urlshortner.model.UrlDao;
import com.example.urlshortner.repository.UrlRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UrlShortenerServiceImpl implements UrlShortnerService {

    private final UrlRepository urlRepository;

    public UrlShortenerServiceImpl(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Override
    @Transactional
    public Integer addNewUrlToShorten(UrlDao urlDao) {
        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setOriginalUrl(urlDao.getLongUrl());
        urlEntity.setShortUrl(urlDao.getShortUrl());
        urlEntity.setUserId(urlDao.getUserId());
        try {
            urlRepository.save(urlEntity);
            log.info("data is saved but not persisted");
//            UrlEntity entity = urlRepository.getReferenceById(urlEntity.getUserId());
        }catch (Exception e) {
            log.error(e.getMessage());
        }
        return urlEntity.getId();
    }
}
