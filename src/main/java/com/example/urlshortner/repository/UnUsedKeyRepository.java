package com.example.urlshortner.repository;

import com.example.urlshortner.entity.UnUsedKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UnUsedKeyRepository extends JpaRepository<UnUsedKeyEntity, Long> {
        @Query(value = "SELECT * FROM un_used_key ORDER BY RAND() LIMIT 1", nativeQuery = true)
        Optional<UnUsedKeyEntity> pickOneUnusedKey();
}
