package com.example.urlshortner.controller;

import com.example.urlshortner.entity.UrlEntity;
import com.example.urlshortner.service.UrlShortnerServiceInterface;
import com.example.urlshortner.model.UrlDao;
import com.example.urlshortner.model.UrlDto;
import com.example.urlshortner.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController("/")
@Slf4j
public class UrlShortnerController {
    private final UrlShortnerServiceInterface urlShortnerServiceInterface;

    public UrlShortnerController(UrlShortnerServiceInterface urlShortnerServiceInterface) {
        this.urlShortnerServiceInterface = urlShortnerServiceInterface;
    }

    @PostMapping("api/v1/post")
    public ResponseEntity postUrlToShorten(@RequestBody UrlDao urlDao) throws Exception{
            if(urlDao.getLongUrl() == null) {
                return new ResponseEntity<>("url required", HttpStatusCode.valueOf(400));
            }
            UrlDto urlDetails = urlShortnerServiceInterface.addNewUrlToShorten(urlDao);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("url-id", urlDetails.getId());
            responseBody.put("short-url", urlDetails.getShortUrl());

            ApiResponse apiResponse = new ApiResponse("Success", responseBody);
            return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(201));

    }

    @GetMapping("api/v1/get")
    public ResponseEntity getShortUrlById(@RequestParam(required = false) Long id) throws Exception{
        Map<String, String> responseBody = new HashMap<>();

        if(id == null) {
            responseBody.put("short-url", null);
            responseBody.put("original-url",null);
            return new ResponseEntity<>(new ApiResponse("id required", responseBody), HttpStatusCode.valueOf(400));
        }

        UrlDto urlDto = urlShortnerServiceInterface.getShortUrlById(id);
        responseBody.put("short-url", urlDto.getShortUrl());
        responseBody.put("original-url", urlDto.getOriginalUrl());

        ApiResponse apiResponse = new ApiResponse("Success", responseBody);
        return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(301));
    }

    @DeleteMapping("api/v1/delete")
    public ResponseEntity deleteShortUrlById(@RequestParam Long id) throws Exception {
        if(id == null) {
            return new ResponseEntity<>("id not found", HttpStatusCode.valueOf(400));
        }
        urlShortnerServiceInterface.deletedShortUrlById(id);
        return new ResponseEntity<>("Url Deleted", HttpStatusCode.valueOf(204));
    }

    @PutMapping("api/v1/modify")
    public ResponseEntity modifyOriginalUrl(@RequestParam("id") Long id, @RequestBody UrlDao urlDao) throws Exception {
        if(urlDao.getLongUrl() == null) {
            return new ResponseEntity<>("require original url", HttpStatusCode.valueOf(400));
        }
        urlShortnerServiceInterface.modifyOriginalUrl(urlDao, id);
        return new ResponseEntity<>("Url Updated", HttpStatusCode.valueOf(200));
    }

}
