package com.example.urlshortner.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;

@Component
public class FunctionUtils {

    private final JwtUtil jwtUtil;

    int KEY_LENGTH = 7;

    public FunctionUtils(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String generateRandomKey() {
        boolean useLetters = true;
        boolean useNumbers = false;
        String generatedString = RandomStringUtils.random(KEY_LENGTH, useLetters, useNumbers);

        System.out.println("generatedString  " + generatedString);
        return generatedString;
    }

    public boolean isValidUrl(String urlString) {
        String regex = "^(https?://)([\\w\\-]+\\.)+[\\w\\-]+(/[\\w\\-./?%&=]*)?$";
        return urlString.matches(regex);
    }
}
