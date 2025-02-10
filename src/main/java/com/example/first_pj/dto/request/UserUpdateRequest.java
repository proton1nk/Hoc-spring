package com.example.first_pj.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

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


     @Data
     @AllArgsConstructor
     @NoArgsConstructor
     @Builder
     @FieldDefaults(level = AccessLevel.PRIVATE)
     public static class AuthenticationRequest {
         String username;
         String password;
     }
}
