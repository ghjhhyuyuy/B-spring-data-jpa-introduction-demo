package com.thoughtworks.capability.gtb.springdatajpaintro.service;

import com.thoughtworks.capability.gtb.springdatajpaintro.dao.EducationDao;
import com.thoughtworks.capability.gtb.springdatajpaintro.dao.UserDao;
import com.thoughtworks.capability.gtb.springdatajpaintro.entity.Education;
import com.thoughtworks.capability.gtb.springdatajpaintro.entity.User;
import com.thoughtworks.capability.gtb.springdatajpaintro.exception.UserNotExistedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
/**
 * Created by wzw on 2020/9/22.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private UserService userService;
    @Mock
    private UserDao userDao;
    private Map<Long, List<Education>> educations = new HashMap<>();
    private User user;
    private List<Education> educationList = new ArrayList<>();
    private Education education;
    @BeforeEach
    void setUp() {
        education = Education.builder()
                .description("just a test")
                .title("test")
                .year(2014L)
                .userId(11L)
                .build();
        educationList.add(education);
        educations.put(education.getUserId(),educationList);
        userService = new UserService(userDao,educations);
        user = User.builder()
                .id(123L)
                .name("Panda")
                .age(24L)
                .avatar("http://...")
                .description("A good guy.")
                .build();
    }

    @Nested
    class FindById {

        @Nested
        class WhenIdExists {

            @Test
            public void should_return_user() {
                when(userDao.findOneById(123L)).thenReturn(Optional.of(user));

                User foundUser = userService.findById(123L);

                assertThat(foundUser).isEqualTo(User.builder()
                        .id(123L)
                        .name("Panda")
                        .age(24L)
                        .avatar("http://...")
                        .description("A good guy.")
                        .build());
            }
        }

        @Nested
        class WhenUserIdNotExisted {

            @Test
            void should_throw_exception() {
                when(userDao.findOneById(222L)).thenReturn(Optional.empty());

                UserNotExistedException thrownException = assertThrows(UserNotExistedException.class, () -> {
                    userService.findById(222L);
                });

                assertThat(thrownException.getMessage()).containsSequence("User Not Found");
            }
        }
    }
    @Nested
    class CreateUser {
        @Test
        public void should_return_user() {
            Long userId = userService.createUser(user);
            assertThat(userId).isEqualTo(123L);
        }
    }

    @Nested
    class GetEducationsForUser {
        @Nested
        class WhenIdExists {

            @Test
            public void should_return_educations() {
                when(userDao.findOneById(11L)).thenReturn(Optional.of(user));
                List<Education> educationResult = userService.getEducationsForUser(11L);
                assertThat(educationResult).isEqualTo(educationList);
            }
        }

        @Nested
        class WhenUserIdNotExisted {

            @Test
            void should_throw_exception() {
                when(userDao.findOneById(222L)).thenReturn(Optional.empty());
                UserNotExistedException thrownException = assertThrows(UserNotExistedException.class, () -> {
                    userService.getEducationsForUser(222L);
                });
                assertThat(thrownException.getMessage()).containsSequence("User Not Found");
            }
        }
    }
    @Nested
    class AddEducationForUser {
        @Test
        public void should_return_educations() {
            when(userDao.findOneById(11L)).thenReturn(Optional.of(user));
            userService.addEducationForUser(11L,education);
            assertThat(educationList.size()).isEqualTo(2);
        }
    }
}