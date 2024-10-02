package com.example.kinobackend.config;

import com.example.kinobackend.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)  // Aktiver @PreAuthorize og @PostAuthorize
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/admin/**").hasRole("ADMIN")  // Kun ADMIN kan tilgå /admin/*
                        .anyRequest().authenticated()  // Alle andre endpoints kræver login
                )
                .formLogin(form -> form
                        .permitAll()  // Tillader alle at bruge login-siden
                )
                .logout(logout -> logout
                        .permitAll()  // Tillader alle at logge ud
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Til password hashing
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Returner din egen UserDetailsService-implementation, hvor brugere og roller hentes
        return new CustomUserDetailsService();
    }
}
