package com.example.urlshortner.service.impl;

import com.example.urlshortner.configs.SecurityConfig;
import com.example.urlshortner.entity.UserEntity;
import com.example.urlshortner.exception.DuplicateUserException;
import com.example.urlshortner.exception.ForbiddenException;
import com.example.urlshortner.model.UserDao;
import com.example.urlshortner.repository.UserRepository;
import com.example.urlshortner.service.UserServiceInterface;
import com.example.urlshortner.utils.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserServiceInterface {
    private final UserRepository userRepository;
    private final SecurityConfig securityConfig;

    private final JwtUtil jwtUtil;

    public UserServiceImpl(UserRepository userRepository, SecurityConfig securityConfig, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.securityConfig = securityConfig;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional
    public void signUp(UserDao userDao) throws DuplicateUserException {
        UserEntity user = userRepository.findByUserName(userDao.getUsername());
        log.info("userEntity, user data access object {}", user);
        if(user == null) {
            UserEntity userEntity = new UserEntity();
            userEntity.setUserName(userDao.getUsername());
            String encodedPass = securityConfig.passwordEncoder().encode(userDao.getPassword());
            userEntity.setPassword(encodedPass);
            userRepository.save(userEntity);
        }else {
            Map<String, String> data = new HashMap<>();
            data.put("detail", "duplicate");
            throw new DuplicateUserException(data);
        }
    }

    @Override
    public void modifyUser(UserDao userDao) {
        Map<String, String> data = new HashMap<>();
        data.put("detail", "forbidden");
        UserEntity user = userRepository.findByUserName(userDao.getUsername());
        if(user == null) {
            throw new ForbiddenException(data);
        }
        if (!securityConfig.passwordEncoder().matches(userDao.getPassword(), user.getPassword())) {
            throw new ForbiddenException(data);
        }
        // Encode the new password and update it
        user.setPassword(securityConfig.passwordEncoder().encode(userDao.getNew_password()));
        userRepository.save(user);
    }

    @Override
    public String loginUser(UserDao userDao) {
        Map<String, String> data = new HashMap<>();
        data.put("detail", "forbidden");
        UserEntity user = userRepository.findByUserName(userDao.getUsername());
        String jwtToken = "";
        if(user == null) {
            throw new ForbiddenException(data);
        }
        if(userDao.getUsername().equals(user.getUserName()) && securityConfig.passwordEncoder().matches(userDao.getPassword(), user.getPassword())) {
            jwtToken = jwtUtil.generateToken(userDao.getUsername());
        }else {
            throw new ForbiddenException(data);
        }
        return jwtToken;
    }

    @Override
    public UserDao getUser(UserDao userDao) {
        UserEntity user = userRepository.findByUserName(userDao.getUsername());
        UserDao usr = new UserDao();
        usr.setPassword(user.getPassword());
        usr.setUsername(user.getUserName());
        return usr;
    }


}
