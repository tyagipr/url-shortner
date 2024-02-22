package com.example.urlshortner.exception;

import java.util.Map;

public class ForbiddenException extends RuntimeException {
    private Map<String, String> detail;

    public ForbiddenException(Map<String, String> detail) {
        super("Forbidden");
        this.detail = detail;
    }

    public Map<String, String> getDetail() {
        return detail;
    }
}
