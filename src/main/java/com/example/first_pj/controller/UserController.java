package com.example.first_pj.controller;

import com.example.first_pj.dto.ApiResponse;
import com.example.first_pj.dto.request.UserCreationRequest;
import com.example.first_pj.dto.request.UserUpdateRequest;
import com.example.first_pj.dto.response.UserResponse;

import com.example.first_pj.service.UserService;
import com.example.first_pj.Entity.User;
import jakarta.validation.Valid;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
     UserService userService;



    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    @GetMapping
    List<User> getUsers() {
        return userService.getuser();
    }
    @GetMapping("/{userID}")
    UserResponse getUser(@PathVariable("userID") String userid) {
        return userService.getUser(userid);

    }
    @PutMapping("/{userID}")
    UserResponse update(@RequestBody UserUpdateRequest request, @PathVariable("userID") String userid) {
        return userService.updateUser(userid, request);
    }
    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable("userId") String userid) {
        userService.deleteUser(userid);
        return "User has been deleted";
    }

    @RestController
    public static class AuthenticationController {


    }
}



