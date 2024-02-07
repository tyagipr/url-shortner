package com.example.urlshortner.controller;

import com.example.urlshortner.interfaces.UrlShortnerService;
import com.example.urlshortner.model.UrlDao;
import com.example.urlshortner.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController("/")
@Slf4j
public class UrlShortnerController {
    private final UrlShortnerService urlShortnerService;

    public UrlShortnerController(UrlShortnerService urlShortnerService) {
        this.urlShortnerService = urlShortnerService;
    }

    @PostMapping("api/v1/post-url")
    public ResponseEntity postUrlToShorten(@RequestBody UrlDao urlDao) throws Exception{
        Integer urlId = urlShortnerService.addNewUrlToShorten(urlDao);

        Map<String, Integer> responseBody = new HashMap<>();
        responseBody.put("short-url-id", urlId);

        ApiResponse apiResponse = new ApiResponse("Success", responseBody);
        return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(201));
    }

}
