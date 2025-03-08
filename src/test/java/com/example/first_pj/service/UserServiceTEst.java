/* (C)2025 */
package com.example.first_pj.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.example.first_pj.Entity.User;
import com.example.first_pj.dto.request.UserCreationRequest;
import com.example.first_pj.dto.response.UserResponse;
import com.example.first_pj.exception.AppException;
import com.example.first_pj.repository.UserRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

<<<<<<< HEAD:src/test/java/com/example/first_pj/service/UserServiceTest.java
@SpringBootTest
@TestPropertySource("/test.properties")
public class UserServiceTest {
=======
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource("/test.properties")
public class UserServiceTEst {
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
>>>>>>> ca9919d166fae06388d0dd0162dd8c0007f2b141:src/test/java/com/example/first_pj/service/UserServiceTEst.java

    @Autowired UserService userService;

    @MockitoBean UserRepository userRepository;
    private UserResponse userResponse;
    private UserCreationRequest request;
    private User user;
    LocalDate dob;

    @BeforeEach
    void initData() {
        dob = LocalDate.of(2005, 1, 1);
        request =
                UserCreationRequest.builder()
                        .lastName("Nguyen")
                        .firstName("Khai")
                        .password("12345678")
                        .username("Khai")
                        .birthday(dob)
                        .build();
        userResponse =
                UserResponse.builder()
                        .id("cf345b4630d")
                        .lastName("Nguyen")
                        .firstName("Khai")
                        .username("Khai")
                        .birthday(dob)
                        .build();
        user =
                User.builder()
                        .id("cf345b4630d")
                        .lastName("Nguyen")
                        .firstName("Khai")
                        .username("Khai")
                        .birthday(dob)
                        .build();
    }

    @Test
    void createUser_validRequest_success() {
        // given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);
        // When
        var response = userService.createUser(request);
        // Then
        Assertions.assertEquals("cf345b4630d", response.getId());
        Assertions.assertEquals("Khai", response.getUsername());
    }

    @Test
    void createUser_validRequest_Fail() {
        // given
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // When
        var exception =
                Assertions.assertThrows(AppException.class, () -> userService.createUser(request));
        // Then
        assertThat(exception.getErrorcode().getCode()).isEqualTo(1002);
    }
}
