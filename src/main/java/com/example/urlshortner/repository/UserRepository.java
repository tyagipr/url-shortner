package com.example.urlshortner.repository;

import com.example.urlshortner.entity.UrlEntity;
import com.example.urlshortner.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query(value = "SELECT * FROM users where user_name=:userName", nativeQuery = true)
    UserEntity findByUserName(String userName);

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity SET password=:password WHERE userName = :userName")
    void updateNewPassword(String password, String userName);
}
