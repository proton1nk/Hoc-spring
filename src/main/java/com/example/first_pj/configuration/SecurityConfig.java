package com.example.first_pj.configuration;

import com.example.first_pj.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    public final String[]PUBLIC_ENDPOINT={"/users","/auth/token","/auth/introspect"};
        @Value("${jwt.signerKey}")
        private String signerKey;
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests(request ->
                    request.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINT).permitAll()

                            .anyRequest().
                            authenticated());
            // Chi co admin moi co the lay tat ca
            http.oauth2ResourceServer(oauth2 ->
                    oauth2.jwt(jwtConfigurer ->
                        jwtConfigurer.decoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
            );

            http.csrf(AbstractHttpConfigurer::disable);
            return http.build();
        }
        // ERROR 401 kh the  xu li o GEH vi no nam tren cac filter truoc khi vao service khac 403

        @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter()
        {
            JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter= new JwtGrantedAuthoritiesConverter();
            jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
            JwtAuthenticationConverter jwtAuthenticationConverter =new JwtAuthenticationConverter();
            jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
            return  jwtAuthenticationConverter;
        }
        @Bean
    JwtDecoder jwtDecoder(){
            SecretKeySpec secretKeySpec= new SecretKeySpec(signerKey.getBytes(),"HS512");
          return NimbusJwtDecoder.
                  withSecretKey(secretKeySpec).
                  macAlgorithm(MacAlgorithm.HS512).build();

        };
        @Bean
        PasswordEncoder passwordEncoder()
        {
            return new BCryptPasswordEncoder(10);
        }
}
