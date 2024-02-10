package com.example.urlshortner.repository;

import com.example.urlshortner.entity.UrlEntity;
import com.example.urlshortner.entity.UsedKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<UrlEntity, Long> {
    @Query(value = "SELECT * FROM urls where original_url=:originalUrl", nativeQuery = true)
    Optional<UrlEntity> findEntryByOriginalUrl(@Param("originalUrl") String originalUrl);

    @Modifying
    @Transactional
    @Query("UPDATE UrlEntity SET originalUrl=:originalUrl WHERE id = :id")
    void updateOriginalUrlById(Long id, String originalUrl);
}
