package com.example.urlshortner.serviceimpl;

import com.example.urlshortner.entity.UrlEntity;
import com.example.urlshortner.interfaces.UrlShortnerService;
import com.example.urlshortner.model.UrlDao;
import com.example.urlshortner.model.UrlDto;
import com.example.urlshortner.repository.UrlRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class UrlShortenerServiceImpl implements UrlShortnerService {

    private final UrlRepository urlRepository;

    public UrlShortenerServiceImpl(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Override
    public UrlDto getShortUrlById(Integer id) {
        Optional<UrlEntity> optionalUrlEntity = urlRepository.findById(id);
        UrlDto urlDto = new UrlDto();
        if(optionalUrlEntity.isPresent()) {
            urlDto.setShortUrl(optionalUrlEntity.get().getShortUrl());
            urlDto.setOriginalUrl(optionalUrlEntity.get().getOriginalUrl());
        }
        return urlDto;
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
        }catch (Exception e) {
            log.error(e.getMessage());
        }
        return urlEntity.getId();
    }


}
