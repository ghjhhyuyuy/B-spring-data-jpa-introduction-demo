package com.thoughtworks.capability.gtb.springdatajpaintro.dao;

import com.thoughtworks.capability.gtb.springdatajpaintro.entity.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryUserDao implements UserDao {
    final AtomicLong userIdSeq = new AtomicLong();
    final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> findOneById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public void save(User user) {
        user.setId(userIdSeq.incrementAndGet());
        users.put(user.getId(), user);
    }
}
