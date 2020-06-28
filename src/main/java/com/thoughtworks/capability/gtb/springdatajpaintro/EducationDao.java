package com.thoughtworks.capability.gtb.springdatajpaintro;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class EducationDao {
    final List<Education> educations = new ArrayList<>();

    public List<Education> findAllByUserId(Long userId) {
        return educations.stream()
                .filter(education -> Objects.equals(userId, education.getUserId()))
                .collect(Collectors.toList());
    }

    public void save(Education education) {
        educations.add(education);
    }
}
