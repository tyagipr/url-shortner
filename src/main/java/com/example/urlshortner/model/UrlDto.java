package com.example.urlshortner.model;

import lombok.Data;

@Data
public class UrlDto {
    Long id;
    String shortUrl;
    String value;
}
