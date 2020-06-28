package com.thoughtworks.capability.gtb.springdatajpaintro;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class UserRepository {
    final AtomicLong userIdSeq = new AtomicLong();
    final Map<Long, User> users = new HashMap<>();

    List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    Optional<User> findOneById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    void save(User user) {
        user.setId(userIdSeq.incrementAndGet());
        users.put(user.getId(), user);
    }
}
