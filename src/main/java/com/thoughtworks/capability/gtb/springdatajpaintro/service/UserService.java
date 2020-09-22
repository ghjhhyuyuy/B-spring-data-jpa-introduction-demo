package com.thoughtworks.capability.gtb.springdatajpaintro.service;

import com.thoughtworks.capability.gtb.springdatajpaintro.dao.UserDao;
import com.thoughtworks.capability.gtb.springdatajpaintro.entity.Education;
import com.thoughtworks.capability.gtb.springdatajpaintro.entity.User;
import com.thoughtworks.capability.gtb.springdatajpaintro.exception.UserNotExistedException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    final UserDao userDao;
    Map<Long, List<Education>> educations = new HashMap<>();

    public UserService(UserDao userDao, Map<Long, List<Education>> educations) {
        this.userDao = userDao;
        this.educations = educations;
    }

    public List<User> findUsers() {
        return userDao.findAll();
    }

    public User findById(Long id) {
        return userDao.findOneById(id)
                .orElseThrow(() -> new UserNotExistedException("User Not Found"));
    }

    public Long createUser(User user) {
        userDao.save(user);
        return user.getId();
    }

    public List<Education> getEducationsForUser(Long userId) {
        findById(userId);
        return educations.get(userId);
    }

    public void addEducationForUser(Long userId, Education education) {
        getEducationsForUser(userId).add(education);
    }
}
