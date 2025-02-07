package com.example.first_pj.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
     String id;
     String username;
     String password;
     String lastName;
     String firstName;
     LocalDate birthday;

}
