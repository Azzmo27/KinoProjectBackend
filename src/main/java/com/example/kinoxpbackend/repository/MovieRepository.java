package com.example.kinoxpbackend.repository;

import com.example.kinoxpbackend.model.Movie;
import com.example.kinoxpbackend.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
    List<Movie> findByTitle(String string);
    boolean existsByImdbID(String imdbId);
    Optional<Movie> findByImdbID(String imdbId);
    List<Movie> findByTitleContaining(String string);
    Movie findMovieByMovieId(int movieId);

}
