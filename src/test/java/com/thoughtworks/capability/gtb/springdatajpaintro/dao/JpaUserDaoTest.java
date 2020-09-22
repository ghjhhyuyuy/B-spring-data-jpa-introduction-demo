package com.thoughtworks.capability.gtb.springdatajpaintro.dao;

import com.thoughtworks.capability.gtb.springdatajpaintro.entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
/**
 * Created by wzw on 2020/9/22.
 */
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class JpaUserDaoTest {
    @Autowired
    private JpaUserDao userDao;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    void should_return_user_when_id_exists() {
        entityManager.persistAndFlush(User.builder()
                .name("Panda")
                .age(24L)
                .avatar("http://...")
                .description("A good guy.")
                .build());

        Optional<User> found = userDao.findOneById(1L);
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get()).isEqualTo(User.builder()
                .id(1L)
                .name("Panda")
                .age(24L)
                .avatar("http://...")
                .description("A good guy.")
                .build());
    }
}