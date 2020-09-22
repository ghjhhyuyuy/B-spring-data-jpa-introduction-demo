package com.thoughtworks.capability.gtb.springdatajpaintro.entity;

import lombok.*;

import javax.persistence.*;

@Data
public class Education {
    private Long userId;
    private Long year;
    private String title;
    private String description;
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
