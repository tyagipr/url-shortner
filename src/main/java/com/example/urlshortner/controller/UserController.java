package com.example.urlshortner.controller;

import com.example.urlshortner.exception.DuplicateUserException;
import com.example.urlshortner.model.UserDao;
import com.example.urlshortner.service.UserServiceInterface;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    private final UserServiceInterface userServiceInterface;

    public UserController(UserServiceInterface userServiceInterface) {
        this.userServiceInterface = userServiceInterface;
    }

    @PostMapping("/users")
    public ResponseEntity postUser(@RequestBody UserDao userDao) throws DuplicateUserException {
        userServiceInterface.signUp(userDao);
        return new ResponseEntity<>(HttpStatusCode.valueOf(201));
    }

    @PutMapping("/users")
    public ResponseEntity modifyUser(@RequestBody UserDao userDao) throws DuplicateUserException {
        userServiceInterface.modifyUser(userDao);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

    @PostMapping("/users/login")
    public ResponseEntity loginUser(@RequestBody UserDao userDao) {
        String jwtToken = userServiceInterface.loginUser(userDao);
        Map<String, String> content = new HashMap<>();
        content.put("token", jwtToken);
        return new ResponseEntity<>(content, HttpStatusCode.valueOf(200));
    }

    @GetMapping("/get")
    public ResponseEntity getUser(@RequestBody UserDao userDao) {
        UserDao userDao1 = userServiceInterface.getUser(userDao);
        return new ResponseEntity<>(userDao1, HttpStatusCode.valueOf(200));
    }
}
