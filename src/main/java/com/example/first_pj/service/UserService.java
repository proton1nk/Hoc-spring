package com.example.first_pj.service;

import com.example.first_pj.Entity.User;
import com.example.first_pj.dto.request.UserCreationRequest;
import com.example.first_pj.dto.request.UserUpdateRequest;
import com.example.first_pj.dto.response.UserResponse;
import com.example.first_pj.enums.Role;
import com.example.first_pj.exception.AppException;
import com.example.first_pj.exception.ErrorCode;
import com.example.first_pj.mapper.UserMapper;
import com.example.first_pj.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    UserMapper userMapper;


    public UserResponse createUser (UserCreationRequest request) {
        if(userRepository.existsByUsername(request.getUsername())) throw new AppException(ErrorCode.USER_EXISITED) ;
        User user = userMapper.toUser(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        HashSet<String> role= new HashSet<>();
        role.add(Role.USER.name());
        user.setRoles(role);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public List<User> getuser() {
        return userRepository.findAll();
    }

    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("NULL ID")));

    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_EXISITED));
        userMapper.updateUser(user, request);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}

