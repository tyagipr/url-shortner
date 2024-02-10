package com.example.urlshortner.service.impl;

import com.example.urlshortner.entity.UnUsedKeyEntity;
import com.example.urlshortner.entity.UrlEntity;
import com.example.urlshortner.entity.UsedKeyEntity;
import com.example.urlshortner.exception.CustomException;
import com.example.urlshortner.repository.UnUsedKeyRepository;
import com.example.urlshortner.repository.UsedKeyRepository;
import com.example.urlshortner.service.KeyGeneratorServiceInterface;
import com.example.urlshortner.service.UrlShortnerServiceInterface;
import com.example.urlshortner.model.UrlDao;
import com.example.urlshortner.model.UrlDto;
import com.example.urlshortner.repository.UrlRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class UrlShortenerServiceImpl implements UrlShortnerServiceInterface {

    private final UrlRepository urlRepository;
    private final UnUsedKeyRepository unUsedKeyRepository;

    private final UsedKeyRepository usedKeyRepository;

    private final KeyGeneratorServiceInterface keyGeneratorService;

    private UrlDto urlDto = new UrlDto();

    private final String URL_DOMAIN_NAME = "small-url.com/";
    public UrlShortenerServiceImpl(UrlRepository urlRepository, UnUsedKeyRepository unUsedKeyRepository, UsedKeyRepository usedKeyRepository, KeyGeneratorServiceInterface keyGeneratorService) {
        this.urlRepository = urlRepository;
        this.unUsedKeyRepository = unUsedKeyRepository;
        this.usedKeyRepository = usedKeyRepository;
        this.keyGeneratorService = keyGeneratorService;
    }

    @Override
    public UrlDto getShortUrlById(Long id) throws Exception {
        Optional<UrlEntity> optionalUrlEntity = urlRepository.findById(id);
        if(optionalUrlEntity.isPresent()) {
            urlDto.setShortUrl(optionalUrlEntity.get().getShortUrl());
            urlDto.setOriginalUrl(optionalUrlEntity.get().getOriginalUrl());
        }else {
            throw new Exception("url not found");
        }
        return urlDto;
    }

    @Override
    public void deletedShortUrlById(Long id) throws Exception {
        try {
            urlRepository.deleteById(id);
        }catch (Exception e) {
            log.error("error in deleting short url for id : {} ", id, e);
            throw new Exception("error in deleting short url");
        }
    }

    @Override
    public void modifyOriginalUrl(UrlDao urlDao , Long id) throws Exception {
        try {
            urlRepository.updateOriginalUrlById(id, urlDao.getLongUrl());
        }catch (Exception e) {
            throw new Exception("error in modifying original url");
        }
    }

    @Override
    @Transactional
    public UrlDto addNewUrlToShorten(UrlDao urlDao) throws Exception {
        UrlEntity urlEntity = new UrlEntity();

        // get a new key from the unused key store
        Optional<UnUsedKeyEntity> unUsedKeyData= unUsedKeyRepository.pickOneUnusedKey();
        String hashKey = "";
        Long hashId;
        if(unUsedKeyData.isPresent()){
            hashKey = unUsedKeyData.get().getHash();
            hashId = unUsedKeyData.get().getId();
        }else {
            throw new CustomException("key not found");
        }

        while(usedKeyRepository.findEntryByHash(hashKey).isPresent()) {
            log.info("hash key already exists");
            hashKey = unUsedKeyRepository.pickOneUnusedKey().get().getHash();
            hashId = unUsedKeyRepository.pickOneUnusedKey().get().getId();
        }

        String shortURl = URL_DOMAIN_NAME + hashKey;

        Optional<UrlEntity> urlEntry = urlRepository.findEntryByOriginalUrl(urlDao.getLongUrl());
        if(urlEntry.isPresent()) {
            urlDto.setOriginalUrl(urlEntry.get().getOriginalUrl());
            urlDto.setShortUrl(urlEntry.get().getShortUrl());
            urlDto.setId(urlEntry.get().getId());
            return urlDto;
        }

        urlEntity.setOriginalUrl(urlDao.getLongUrl());
        urlEntity.setShortUrl(shortURl);
        urlEntity.setUserId(urlDao.getUserId());

        // save in used repository
        UsedKeyEntity usedKeyEntity = new UsedKeyEntity();
        usedKeyEntity.setHash(hashKey);
        usedKeyEntity.setUsed(true);
        usedKeyRepository.save(usedKeyEntity);

        // remove from unused repository
        unUsedKeyRepository.deleteById(hashId);
        keyGeneratorService.generateNewKeys();

        try {
            urlRepository.save(urlEntity);
            log.info("data is saved in url repository :::", urlEntity);
        }catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException("error in saving url data");
        }
        urlDto.setOriginalUrl(urlEntity.getOriginalUrl());
        urlDto.setShortUrl(urlEntity.getShortUrl());
        urlDto.setId(urlEntity.getId());
        return urlDto;
    }


}
