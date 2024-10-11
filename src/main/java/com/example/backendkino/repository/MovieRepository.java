package com.example.backendkino.repository;

import com.example.backendkino.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Integer> {

    Movie findMovieByMovieId(int movieId);

    List<Movie> findByTitleContaining(String string);

    boolean existsByImdbID(String imdbId);

}
