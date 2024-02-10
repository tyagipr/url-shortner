package com.example.urlshortner.intrface;


import com.example.urlshortner.service.KeyGeneratorServiceInterface;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitialKeyGenerator implements CommandLineRunner {

    private final KeyGeneratorServiceInterface keyGeneratorService;

    public InitialKeyGenerator(KeyGeneratorServiceInterface keyGeneratorService) {
        this.keyGeneratorService = keyGeneratorService;
    }

    @Override
    public void run(String... args) throws Exception {
        keyGeneratorService.generateNewKeys();
    }
}
