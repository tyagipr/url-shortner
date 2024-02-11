package com.example.urlshortner.model;

import lombok.Data;

@Data
public class UrlDao {
    String value;
    String url;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
