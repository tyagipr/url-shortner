package com.example.urlshortner.service.impl;

import com.example.urlshortner.entity.UnUsedKeyEntity;
import com.example.urlshortner.repository.UnUsedKeyRepository;
import com.example.urlshortner.service.KeyGeneratorServiceInterface;
import com.example.urlshortner.utils.FunctionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class KeyGeneratorServiceImpl implements KeyGeneratorServiceInterface {


    private final FunctionUtils functionUtils;
    private final UnUsedKeyRepository unUsedKeyRepository;

    public KeyGeneratorServiceImpl(FunctionUtils functionUtils, UnUsedKeyRepository unUsedKeyRepository) {
        this.functionUtils = functionUtils;
        this.unUsedKeyRepository = unUsedKeyRepository;
    }


    @Override
    public void generateNewKeys() {
        int count=0;
        if(unUsedKeyRepository.count() > 2) {
            log.info("returning because key count is greater than 2");
            return;
        }
        while(true) {
            if(count == 10) {
                log.info("returning because key count is 10");
                break;
            }
            UnUsedKeyEntity unUsedKeyEntity = new UnUsedKeyEntity();
            unUsedKeyEntity.setHash(functionUtils.generateRandomKey());
            unUsedKeyEntity.setUsed(false);
            unUsedKeyRepository.save(unUsedKeyEntity);
            count++;
        }

    }
}
