package com.example.urlshortner.exception;

import java.util.Map;

public class DuplicateUserException extends RuntimeException {

    private Map<String, String> detail;

    public DuplicateUserException(Map<String, String> detail) {
        super("Duplicate Request Error.");
        this.detail = detail;
    }

    public Map<String, String> getDetail() {
        return detail;
    }
}
