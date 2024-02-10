package com.example.urlshortner.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "used_key")
public class UsedKeyEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String hash;
    private Boolean isUsed;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public Long getRemainCnt() {
        return remainCnt;
    }

    public void setRemainCnt(Long remainCnt) {
        this.remainCnt = remainCnt;
    }

    private Long remainCnt;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
