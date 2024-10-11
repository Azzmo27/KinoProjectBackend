package com.example.kinoxpbackend.repository;

import com.example.kinoxpbackend.model.Admin;
import com.example.kinoxpbackend.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByUsername(String userName);
}
