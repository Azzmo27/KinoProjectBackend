package com.example.kinobackend.service;

import com.example.kinobackend.model.Role;
import com.example.kinobackend.model.User;
import com.example.kinobackend.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        insertInitialData();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }


        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singleton(user.getRole())
        );
    }


    public void insertInitialData() {
        System.out.println("Initializing test data...");

        String userSql = "INSERT INTO users (password, role, username) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE username = username"; // Adjust this as needed
        jdbcTemplate.update(userSql, "password123", Role.ADMIN.name(), "john_doe");


        String checkCustomerSql = "SELECT COUNT(*) FROM customer WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(checkCustomerSql, Integer.class, "lars@example.com");

        if (count == null || count == 0) {
            String customerSql = "INSERT INTO customer (email, first_name, last_name, password, role, user_name) VALUES (?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(customerSql, "lars@example.com", "lars", "x", "PASSLARS123", Role.CUSTOMER.name(), "Lars_user1");
            System.out.println("Customer data inserted successfully.");
        } else {
            System.out.println("Customer with this email already exists");
        }

        System.out.println("Test data inserted successfully.");
    }


}