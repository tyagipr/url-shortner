package com.example.urlshortner.repository;

import com.example.urlshortner.entity.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlRepository extends JpaRepository<UrlEntity, Integer> {
}
