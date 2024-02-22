package com.example.urlshortner.handler;

import com.example.urlshortner.exception.CustomException;
import com.example.urlshortner.exception.DuplicateUserException;
import com.example.urlshortner.exception.ForbiddenException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> commonExceptionHandler(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatusCode.valueOf(404));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleCustomException(CustomException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatusCode.valueOf(400));
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateUserException(DuplicateUserException ex) {
        return new ResponseEntity<>(ex.getDetail(), HttpStatusCode.valueOf(409));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Map<String, String>> handleForbiddenException(ForbiddenException ex) {
        return new ResponseEntity<>(ex.getDetail(), HttpStatusCode.valueOf(403));
    }

}
