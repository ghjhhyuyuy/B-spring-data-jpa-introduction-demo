package com.thoughtworks.capability.gtb.springdatajpaintro.controller;

import com.thoughtworks.capability.gtb.springdatajpaintro.service.UserService;
import com.thoughtworks.capability.gtb.springdatajpaintro.entity.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.findUsers();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping("/users")
    public Long createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

}
