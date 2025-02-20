package com.example.first_pj.dto.request;

import com.example.first_pj.Entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
     String username;
     String password;
     String lastName;
     String firstName;
     LocalDate birthday;

    List<String> roles;


}
