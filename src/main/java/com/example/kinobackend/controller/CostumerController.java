package com.example.kinobackend.controller;

import com.example.kinobackend.model.Movie;
import com.example.kinobackend.model.Showing;
import com.example.kinobackend.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
public class CostumerController {

    @Autowired
    private MovieService movieService;

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieService.getMoviesWithShowings();
        return ResponseEntity.ok(movies);
    }


    @GetMapping("/upcoming")
    public ResponseEntity<List<Movie>> getUpcomingMovies() {
        List<Movie> upcomingMovies = movieService.getUpcomingMoviesWithinThreeMonths();
        return ResponseEntity.ok(upcomingMovies);
    }

    @GetMapping("/{id}/showings")
    public ResponseEntity<List<Showing>> getShowingsByMovieId(@PathVariable int id) {
        List<Showing> showings = movieService.getShowingsForMovie(id);
        return ResponseEntity.ok(showings);

    }

}
