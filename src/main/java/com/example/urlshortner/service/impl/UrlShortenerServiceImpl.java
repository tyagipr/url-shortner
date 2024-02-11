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
import com.example.urlshortner.utils.FunctionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UrlShortenerServiceImpl implements UrlShortnerServiceInterface {

    private final UrlRepository urlRepository;
    private final UnUsedKeyRepository unUsedKeyRepository;

    private final UsedKeyRepository usedKeyRepository;

    private final KeyGeneratorServiceInterface keyGeneratorService;
    private final FunctionUtils functionUtils;

    private UrlDto urlDto = new UrlDto();

    private final String URL_DOMAIN_NAME = "https://short-url/";
    public UrlShortenerServiceImpl(UrlRepository urlRepository, UnUsedKeyRepository unUsedKeyRepository, UsedKeyRepository usedKeyRepository, KeyGeneratorServiceInterface keyGeneratorService, FunctionUtils functionUtils) {
        this.urlRepository = urlRepository;
        this.unUsedKeyRepository = unUsedKeyRepository;
        this.usedKeyRepository = usedKeyRepository;
        this.keyGeneratorService = keyGeneratorService;
        this.functionUtils = functionUtils;
    }

    @Override
    public UrlDto getShortUrlById(Long id) throws Exception {
        Optional<UrlEntity> optionalUrlEntity = urlRepository.findById(id);
        if(optionalUrlEntity.isPresent()) {
            urlDto.setShortUrl(optionalUrlEntity.get().getShortUrl());
            urlDto.setValue(optionalUrlEntity.get().getOriginalUrl());
        }else {
            throw new Exception("url not found");
        }
        return urlDto;
    }

    @Override
    public void deleteUrlById(Long id) throws Exception {
        if(urlRepository.existsById(id)) {
            urlRepository.deleteById(id);
        }else {
            throw new Exception("id does not exist");
        }

    }

    @Override
    public void modifyOriginalUrl(String updatedUrl , Long id) throws CustomException {
        if(functionUtils.isValidUrl(updatedUrl)) {
            urlRepository.updateOriginalUrlById(id, updatedUrl);
        }else {
            throw new CustomException("invalid url");
        }
    }

    @Override
    public List<UrlDto> getAllEntries() {
        List<UrlEntity> allUrlsEntries = urlRepository.findAll();
        List<UrlDto> urlDtoList = new ArrayList<>();
        for(UrlEntity urlEntity : allUrlsEntries) {
            UrlDto urlObj = new UrlDto();
            urlObj.setValue(urlEntity.getOriginalUrl());
            urlDtoList.add(urlObj);
        }
        return urlDtoList;
    }

    @Override
    public void deleteAllEntries() {
        urlRepository.deleteAll();
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

        Optional<UrlEntity> urlEntry = urlRepository.findEntryByOriginalUrl(urlDao.getValue());
        if(urlEntry.isPresent()) {
            urlDto.setValue(urlEntry.get().getOriginalUrl());
            urlDto.setShortUrl(urlEntry.get().getShortUrl());
            urlDto.setId(urlEntry.get().getId());
            return urlDto;
        }

        urlEntity.setOriginalUrl(urlDao.getValue());
        urlEntity.setShortUrl(shortURl);

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
        urlDto.setValue(urlEntity.getOriginalUrl());
        urlDto.setShortUrl(urlEntity.getShortUrl());
        urlDto.setId(urlEntity.getId());
        return urlDto;
    }


}
