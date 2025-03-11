package com.example.first_pj.configuration;


import com.example.first_pj.Entity.Role;
import com.example.first_pj.Entity.User;
import com.example.first_pj.constrant.PredefineRole;
import com.example.first_pj.repository.RoleRepository;
import com.example.first_pj.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
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


    @NonFinal
    static final String ADMIN_USER_NAME = "admin";

    @NonFinal
    static final String ADMIN_PASSWORD = "admin";
    PasswordEncoder passwordEncoder;
        @Bean
        @ConditionalOnProperty(prefix = "spring",
                value = "datasource.driverClassName",
                havingValue = "com.mysql.cj.jdbc.Driver"
        )
        ApplicationRunner applicationRunner(UserRepository userRepository,
                                            RoleRepository roleRepository){
            return args -> {
                if(userRepository.findByUsername(ADMIN_USER_NAME).isEmpty())
                {  //Tao quyen user trong database
                    roleRepository.save(Role.builder()
                                .name(PredefineRole.USER_ROLE)
                                .description("User role")
                        .build());
                    //Tao quyen admin trong database
                   Role adminRole= roleRepository.save( Role.builder()
                            .name(PredefineRole.ADMIN_ROLE)
                            .description("Admin role")
                            .build());
                    var roles = new HashSet<Role>();
                    roles.add(adminRole);

                    //add quyen admin
                   User user = User.builder()
                           .username(PredefineRole.ADMIN_ROLE)
                           .password(passwordEncoder.encode(ADMIN_PASSWORD))
                           .roles(roles)
                           .build() ;
                  log.warn("admin have been created with default password is admin, please change it ");
                }
            };
        }
}
