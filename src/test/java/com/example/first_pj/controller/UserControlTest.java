package com.example.first_pj.controller;

import com.example.first_pj.dto.request.UserCreationRequest;
import com.example.first_pj.dto.response.UserResponse;
import com.example.first_pj.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
@Slf4j
@AutoConfigureMockMvc
@SpringBootTest
@TestPropertySource("/test.properties")
public class UserControlTest {

    @Autowired
    private MockMvc Mockmvc;

    @MockitoBean
    private UserService userService;

    private UserCreationRequest request;
    private UserResponse userResponse;
    private LocalDate dob;
    @BeforeEach
    void init_Data ()
    {
        dob = LocalDate.of(2005,1,6);
        request = UserCreationRequest.builder()
                .lastName("Nguyen")
                .firstName("Khai")
                .password("12345678")
                .username("Khai")
                .birthday(dob)
                .build();
        userResponse = UserResponse.builder()
                .id("cf345b4630d")
                .lastName("Nguyen")
                .firstName("Khai")
                .username("Khai")
                .birthday(dob)
                .build();
    }
    @Test
    void createUser_validRequest_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(userResponse);

        // WHEN, THEN
        Mockmvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("result.id").value("cf0600f538b3"));
    }
    @Test
    void createUser_usernameInValid () throws Exception {
        request.setUsername("Joh");
        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        Mockmvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1003))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Username must be at least 4 characters")
    );

    }
}
