package com.thoughtworks.capability.gtb.springdatajpaintro.controller;

import com.thoughtworks.capability.gtb.springdatajpaintro.entity.Education;
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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by wzw on 2020/9/22.
 */
@WebMvcTest(EducationController.class)
@AutoConfigureJsonTesters
class EducationControllerTest {
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JacksonTester<List<Education>> userJson;
    @Autowired
    private JacksonTester<Education> educationJson;
    private List<Education> educations = new ArrayList<>();

    @BeforeEach
    public void beforeEach() {
        Education education = Education.builder()
        .description("just a test")
        .title("test")
        .year(2014L)
        .userId(11L)
        .build();
        educations.add(education);
    }

    @AfterEach
    public void afterEach() {
        Mockito.reset(userService);
    }
    @Nested
    class GetEducationByUserId {

        @Nested
        class WhenUserIdExists {

            @Test
            public void should_return_education_by_id_with_jsonPath() throws Exception {
                when(userService.getEducationsForUser(11L)).thenReturn(educations);

                mockMvc.perform(get("/users/{id}/educations",11))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$[0].userId", is(11)));

                verify(userService).getEducationsForUser(11L);
            }

        }

        @Nested
        class WhenUserIdNotExisted {

            @Test
            public void should_return_NOT_FOUND() throws Exception {
                when(userService.getEducationsForUser(123L)).thenThrow(new UserNotExistedException("User Not Found"));

                mockMvc.perform(get("/users/{id}/educations", 123L))
                        .andExpect(status().isNotFound())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.message", containsString("User Not Found")));

                verify(userService).getEducationsForUser(123L);
            }
        }
    }
    @Nested
    class addEducationForUser {

        private Education nextEducation;

        @BeforeEach
        public void beforeEach() {
            nextEducation = Education.builder()
                    .description("just a test2")
                    .title("test2")
                    .year(2015L)
                    .userId(11L)
                    .build();
        }

        @Nested
        class WhenRequestIsValid {

            @Test
            public void should_add_education() throws Exception {
                //when(userService.addEducationForUser(nextEducation.getUserId(),nextEducation)).thenReturn(true);

                MockHttpServletRequestBuilder requestBuilder = post("/users/{userId}/educations",11)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(educationJson.write(nextEducation).getJson());

                MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                        .andReturn()
                        .getResponse();

                assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

                verify(userService).addEducationForUser(nextEducation.getUserId(),nextEducation);
            }
        }
    }
}