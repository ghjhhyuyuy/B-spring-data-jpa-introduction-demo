package com.thoughtworks.capability.gtb.springdatajpaintro;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class UserDao {
    final AtomicLong userIdSeq = new AtomicLong();
    final List<User> users = new ArrayList<>();

    public List<User> findAll() {
        return users;
    }

    public Optional<User> findOneById(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    public void save(User user) {
        user.setId(userIdSeq.incrementAndGet());
        users.add(user);
    }
}
