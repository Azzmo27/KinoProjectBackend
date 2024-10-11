package com.example.kinoxpbackend.repository;

import com.example.kinoxpbackend.model.Director;
import com.example.kinoxpbackend.model.Director;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectorRepository extends JpaRepository<Director, Integer> {
    Director findDirectorByFullName(String fullName);
}
