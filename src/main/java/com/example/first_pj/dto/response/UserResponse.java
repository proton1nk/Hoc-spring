package com.example.first_pj.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
     String id;
     String username;

     String lastName;
     String firstName;
     LocalDate birthday;
     Set<RoleResponse> roles;

}
