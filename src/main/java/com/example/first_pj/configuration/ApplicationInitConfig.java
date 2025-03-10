package com.example.first_pj.configuration;

import com.example.first_pj.Entity.User;
import com.example.first_pj.enums.Role;
import com.example.first_pj.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.HashSet;


@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;
        @Bean
        @ConditionalOnProperty(prefix = "spring",
                value = "datasource.driverClassName",
                havingValue = "com.mysql.cj.jdbc.Driver"
        )
        ApplicationRunner applicationRunner(UserRepository userRepository){
            return args -> {
            if(userRepository.findByUsername("admin").isEmpty()){
                var roles = new HashSet<String>();
                roles.add(Role.ADMIN.name());

                User user = User.
                        builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        //.roles(roles)
                        .build();

                    userRepository.save(user);
                  log.warn("admin have been created with default password is admin, please change it ");
                }
            };
        }
}
