package com.example.urlshortner.service;

import com.example.urlshortner.exception.DuplicateUserException;
import com.example.urlshortner.model.UserDao;

public interface UserServiceInterface {

    void signUp(UserDao userDao) throws DuplicateUserException;

    void modifyUser(UserDao userDao);

    String loginUser(UserDao userDao);

    UserDao getUser(UserDao userDao);

}
