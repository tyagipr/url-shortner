package com.example.urlshortner.controller;

import com.example.urlshortner.exception.CustomException;
import com.example.urlshortner.model.UpdateUrlData;
import com.example.urlshortner.service.UrlShortnerServiceInterface;
import com.example.urlshortner.model.UrlDao;
import com.example.urlshortner.model.UrlDto;
import com.example.urlshortner.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController("/")
@Slf4j
public class UrlShortnerController {
    private final UrlShortnerServiceInterface urlShortnerServiceInterface;

    public UrlShortnerController(UrlShortnerServiceInterface urlShortnerServiceInterface) {
        this.urlShortnerServiceInterface = urlShortnerServiceInterface;
    }

    @PostMapping("")
    public ResponseEntity postUrlToShorten(@RequestBody UrlDao urlDao) throws Exception {
//            if(urlDao.getValue() == "") {
//                return new ResponseEntity<>("url required", HttpStatusCode.valueOf(400));
//            }
            UrlDto urlDetails = urlShortnerServiceInterface.addNewUrlToShorten(urlDao);


            UrlDto responseBody = new UrlDto();
            responseBody.setId(urlDetails.getId());

            return new ResponseEntity<>(responseBody, HttpStatusCode.valueOf(201));

    }

    @GetMapping("{id}")
    public ResponseEntity getShortUrlById(@PathVariable("id") Long id) throws Exception{
        Map<String, String> responseBody = new HashMap<>();

        if(id == null) {
            return new ResponseEntity<>("id required", HttpStatusCode.valueOf(400));
        }

        UrlDto urlDto = urlShortnerServiceInterface.getShortUrlById(id);

        return new ResponseEntity<>(urlDto, HttpStatusCode.valueOf(301));
    }

    @GetMapping("")
    public ResponseEntity getAllEntries() throws Exception {
        List<UrlDto> allEntries = urlShortnerServiceInterface.getAllEntries();
        if(allEntries.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("value", null);
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(201));
        }
        return new ResponseEntity<>(allEntries, HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteUrlById(@PathVariable("id") Long id) throws Exception {
        if(id == null) {
            return new ResponseEntity<>("id not found", HttpStatusCode.valueOf(404));
        }
        urlShortnerServiceInterface.deleteUrlById(id);
        return new ResponseEntity<>("Url Deleted", HttpStatusCode.valueOf(204));
    }

    @PutMapping(value = "{id}" , consumes = "application/json")
    public ResponseEntity modifyOriginalUrl(@PathVariable("id") Long id, @RequestBody UpdateUrlData urlData) throws Exception, CustomException {
        urlShortnerServiceInterface.modifyOriginalUrl(urlData.getUrl(), id);
        return new ResponseEntity<>("Url Updated", HttpStatusCode.valueOf(200));
    }

    @DeleteMapping(value = "", produces = "application/json")
    public ResponseEntity deleteAllUrl() {
        urlShortnerServiceInterface.deleteAllEntries();
        return new ResponseEntity<>("All Urls Deleted", HttpStatusCode.valueOf(404));
    }
}
