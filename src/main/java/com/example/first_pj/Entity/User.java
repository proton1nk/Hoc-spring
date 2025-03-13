package com.example.first_pj.Entity;


import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;
import java.time.LocalDate;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    //Set uni cho username de khi post nhieu username 1 luc kh co tinh trang bi loi
    @Column(name = "username", unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String username;
    String password;
    String lastName;
    String firstName;
    LocalDate birthday;
    @ManyToMany
    Set<Role> roles;


}
