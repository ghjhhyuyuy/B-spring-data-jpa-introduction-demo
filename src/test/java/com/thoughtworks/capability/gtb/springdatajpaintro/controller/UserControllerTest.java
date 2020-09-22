package com.thoughtworks.capability.gtb.springdatajpaintro.controller;

import com.thoughtworks.capability.gtb.springdatajpaintro.entity.User;
import com.thoughtworks.capability.gtb.springdatajpaintro.exception.UserNotExistedException;
import com.thoughtworks.capability.gtb.springdatajpaintro.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Created by wzw on 2020/9/22.
 */
@WebMvcTest(UserController.class)
@AutoConfigureJsonTesters
class UserControllerTest {
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JacksonTester<User> userJson;

    private User firstUser;

    @BeforeEach
    public void beforeEach() {
        firstUser = User.builder()
                .id(123L)
                .name("Panda")
                .age(24L)
                .avatar("http://...")
                .description("A good guy.")
                .build();
    }

    @AfterEach
    public void afterEach() {
        Mockito.reset(userService);
    }
    @Nested
    class GetUserById {

        @Nested
        class WhenUserIdExists {

            @Test
            public void should_return_user_by_id_with_jsonPath() throws Exception {
                when(userService.findById(123L)).thenReturn(firstUser);

                mockMvc.perform(get("/users/{id}", 123L))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.name", is("Panda")));

                verify(userService).findById(123L);
            }

            @Test
            public void should_return_user_by_id_with_jackson_tester() throws Exception {
                when(userService.findById(123L)).thenReturn(firstUser);

                MockHttpServletResponse response = mockMvc.perform(get("/users/{id}", 123))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andReturn()
                        .getResponse();

                assertThat(response.getContentAsString()).isEqualTo(
                        userJson.write(firstUser).getJson());

                verify(userService).findById(123L);
            }
        }

        @Nested
        class WhenUserIdNotExisted {

            @Test
            public void should_return_NOT_FOUND() throws Exception {
                when(userService.findById(123L)).thenThrow(new UserNotExistedException("User Not Found"));

                mockMvc.perform(get("/users/{id}", 123L))
                        .andExpect(status().isNotFound())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.message", containsString("User Not Found")));

                verify(userService).findById(123L);
            }
        }
    }
    @Nested
    class CreateUser {

        private User newUserRequest;

        @BeforeEach
        public void beforeEach() {
            newUserRequest = User.builder()
                    .name("Panda")
                    .age(24L)
                    .avatar("http://...")
                    .description("A good guy.")
                    .build();
        }

        @Nested
        class WhenRequestIsValid {

            @Test
            public void should_create_new_user_and_return_its_id() throws Exception {
                when(userService.createUser(newUserRequest)).thenReturn(666L);

                MockHttpServletRequestBuilder requestBuilder = post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson.write(newUserRequest).getJson());

                MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                        .andReturn()
                        .getResponse();

                assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

                verify(userService).createUser(newUserRequest);
            }
        }
    }
}