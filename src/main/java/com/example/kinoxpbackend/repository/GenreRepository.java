package com.example.kinoxpbackend.repository;

import com.example.kinoxpbackend.model.Genre;
import com.example.kinoxpbackend.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Integer> {
    Genre findByGenreName(String genreName);
}
