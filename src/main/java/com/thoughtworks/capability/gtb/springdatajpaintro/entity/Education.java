package com.thoughtworks.capability.gtb.springdatajpaintro.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Entity
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long year;
    private String title;
    private String description;
    @ManyToOne
    private User user;
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
