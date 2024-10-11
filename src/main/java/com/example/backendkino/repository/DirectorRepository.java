package com.example.backendkino.repository;

import com.example.backendkino.model.Director;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectorRepository extends JpaRepository<Director, Integer> {
    Director findDirectorByFullName(String fullName);
}