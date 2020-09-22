package com.thoughtworks.capability.gtb.springdatajpaintro.controller;

import com.thoughtworks.capability.gtb.springdatajpaintro.service.UserService;
import com.thoughtworks.capability.gtb.springdatajpaintro.entity.Education;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EducationController {

    private final UserService userService;

    public EducationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{userId}/educations")
    public List<Education> getEducationsByUserId(@PathVariable Long userId) {
        return userService.getEducationsForUser(userId);
    }

    @PostMapping("/users/{userId}/educations")
    @ResponseStatus(HttpStatus.CREATED)
    public void addEducationForUser(@PathVariable Long userId, @RequestBody Education education) {
        education.setUserId(userId);
        userService.addEducationForUser(userId, education);
    }
}
