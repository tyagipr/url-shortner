package com.example.urlshortner.response;

public class ApiResponse {
    private String text;
    private Object json;

    public ApiResponse(String text, Object json) {
        this.text = text;
        this.json = json;
    }

    // Getters and setters
    public String getText() {
        return text;
    }

    public void setText(String message) {
        this.text = message;
    }

    public Object getJson() {
        return json;
    }

    public void setJson(Object json) {
        this.json = json;
    }
}

