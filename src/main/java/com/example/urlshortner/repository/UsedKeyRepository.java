package com.example.urlshortner.repository;

import com.example.urlshortner.entity.UsedKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsedKeyRepository extends JpaRepository<UsedKeyEntity, Long> {

    @Query(value = "SELECT * FROM used_key where hash=:hash", nativeQuery = true)
    Optional<UsedKeyEntity> findEntryByHash(@Param("hash") String hash);

}
