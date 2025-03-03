package com.example.first_pj.service;

import com.example.first_pj.Entity.User;
import com.example.first_pj.dto.request.UserCreationRequest;
import com.example.first_pj.dto.response.UserResponse;
import com.example.first_pj.exception.AppException;
import com.example.first_pj.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource("/test.properties")
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @MockitoBean
    private UserRepository userRepository;
    private UserCreationRequest request;
    private UserResponse userResponse;
    private User user;
    private LocalDate dob;
    @BeforeEach
    void init_Data() {
        dob = LocalDate.of(2005, 1, 6);
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
        user = User.builder()
                .id("cf345b4630d")
                .lastName("Nguyen")
                .firstName("Khai")
                .username("Khai")
                .birthday(dob)
                .build();

    }

    @Test
    void createUser_validRequest_success() {
        //Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);

        //When
        var response = userService.createUser(request);

        Assertions.assertEquals("cf345b4630d", response.getId());
        Assertions.assertEquals("Khai", response.getUsername());


    }
    @Test

    void createUser_userExisted_fail() {
        // GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // WHEN
        var exception = assertThrows(AppException.class, () -> userService.createUser(request));

        // THEN
        assertThat(exception.getErrorcode().getCode()).isEqualTo(1002);
    }

}
