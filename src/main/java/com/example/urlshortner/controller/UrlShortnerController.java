package com.example.urlshortner.controller;

import com.example.urlshortner.exception.CustomException;
import com.example.urlshortner.model.UpdateUrlData;
import com.example.urlshortner.service.UrlShortnerServiceInterface;
import com.example.urlshortner.model.UrlDao;
import com.example.urlshortner.model.UrlDto;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity postUrlToShorten(@RequestBody UrlDao urlDao, HttpServletRequest request) throws Exception {
            if(request.getAttribute("invalid_token_error") == "Forbidden") {
                return new ResponseEntity<>("Forbidden", HttpStatusCode.valueOf(403));
            }
            if(urlDao.getValue() == "") {
                return new ResponseEntity<>("url required", HttpStatusCode.valueOf(400));
            }
            UrlDto urlDetails = urlShortnerServiceInterface.addNewUrlToShorten(urlDao);


            UrlDto responseBody = new UrlDto();
            responseBody.setId(urlDetails.getId());

            return new ResponseEntity<>(responseBody, HttpStatusCode.valueOf(201));

    }

    @GetMapping("{id}")
    public ResponseEntity getShortUrlById(HttpServletRequest request, @PathVariable("id") Long id) throws Exception{
       log.info("checking headers {} ", request.getHeader("Authorization"));
        if(request.getAttribute("invalid_token_error") == "Forbidden") {
            return new ResponseEntity<>("Forbidden", HttpStatusCode.valueOf(403));
        }
        if(id == null) {
            return new ResponseEntity<>("id required", HttpStatusCode.valueOf(400));
        }

        UrlDto urlDto = urlShortnerServiceInterface.getShortUrlById(id);

        return new ResponseEntity<>(urlDto, HttpStatusCode.valueOf(301));
    }

    @GetMapping("")
    public ResponseEntity getAllEntries(HttpServletRequest request) throws Exception {
        if(request.getAttribute("invalid_token_error") == "Forbidden") {
            return new ResponseEntity<>("Forbidden", HttpStatusCode.valueOf(403));
        }
        List<UrlDto> allEntries = urlShortnerServiceInterface.getAllEntries();
        if(allEntries.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("value", null);
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(201));
        }
        return new ResponseEntity<>(allEntries, HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteUrlById(HttpServletRequest request, @PathVariable("id") Long id) throws Exception {
        log.info("headers in delete by id {}", request.getAttribute("invalid_token_error"));
        if(request.getAttribute("invalid_token_error") == "Forbidden") {
            return new ResponseEntity<>("Forbidden", HttpStatusCode.valueOf(403));
        }
        if(id == null) {
            return new ResponseEntity<>("id not found", HttpStatusCode.valueOf(404));
        }
        urlShortnerServiceInterface.deleteUrlById(id);
        return new ResponseEntity<>("Url Deleted", HttpStatusCode.valueOf(204));
    }

    @PutMapping(value = "{id}" , consumes = "application/json")
    public ResponseEntity modifyOriginalUrl(@PathVariable("id") Long id, @RequestBody UpdateUrlData urlData, HttpServletRequest request) throws Exception, CustomException {
        if(request.getAttribute("invalid_token_error") == "Forbidden") {
            return new ResponseEntity<>("Forbidden", HttpStatusCode.valueOf(403));
        }
        urlShortnerServiceInterface.modifyOriginalUrl(urlData.getUrl(), id);
        return new ResponseEntity<>("Url Updated", HttpStatusCode.valueOf(200));
    }

    @DeleteMapping(value = "", produces = "application/json")
    public ResponseEntity deleteAllUrl(HttpServletRequest request) {
        if(request.getAttribute("invalid_token_error") == "Forbidden") {
            return new ResponseEntity<>("Forbidden", HttpStatusCode.valueOf(403));
        }
        urlShortnerServiceInterface.deleteAllEntries();
        return new ResponseEntity<>("All Urls Deleted", HttpStatusCode.valueOf(404));
    }
}
