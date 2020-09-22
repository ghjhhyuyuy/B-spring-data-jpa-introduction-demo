package com.thoughtworks.capability.gtb.springdatajpaintro.dao;

import com.thoughtworks.capability.gtb.springdatajpaintro.entity.Education;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class EducationDao {
    final Map<Long, List<Education>> educations = new HashMap<>();

    public List<Education> findAllByUserId(Long userId) {
        return educations.get(userId);
    }

    public void save(Education education) {
        List<Education> userEducations = this.educations.get(education.getUserId());
        if (Objects.isNull(userEducations)) {
            userEducations = new ArrayList<>();
            this.educations.put(education.getUserId(), userEducations);
        }
        userEducations.add(education);
    }
}
