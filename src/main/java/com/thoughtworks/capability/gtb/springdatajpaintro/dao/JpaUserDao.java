package com.thoughtworks.capability.gtb.springdatajpaintro.dao;

import com.thoughtworks.capability.gtb.springdatajpaintro.entity.User;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Component
@Primary
public class JpaUserDao implements UserDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<User> findAll() {
        List<?> users = em.createQuery("from " + User.class.getName())
                .getResultList();
        return (List<User>) users;
    }

    @Override
    public Optional<User> findOneById(Long id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

    @Transactional
    @Override
    public void save(User user) {
        em.persist(user);
    }
}
