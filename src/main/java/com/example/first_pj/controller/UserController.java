package com.example.first_pj.controller;

import com.example.first_pj.dto.ApiResponse;
import com.example.first_pj.dto.request.UserCreationRequest;
import com.example.first_pj.dto.request.UserUpdateRequest;
import com.example.first_pj.dto.response.UserResponse;
import com.example.first_pj.exception.AppException;
import com.example.first_pj.exception.ErrorCode;
import com.example.first_pj.mapper.UserMapper;
import com.example.first_pj.repository.UserRepository;
import com.example.first_pj.service.UserService;
import com.example.first_pj.Entity.User;
    import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private UserService userService;
    @Autowired
     UserController(UserService userService, UserRepository userRepository, UserMapper userMapper) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
    @PostMapping

    public User createUser(@RequestBody @Valid UserCreationRequest request) {
        if(userRepository.existsByUsername(request.getUsername())) throw new AppException(ErrorCode.USER_EXISITED);
        User user = userMapper.toUser(request);
        return userRepository.save(user);
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
}



