package com.example.kinobackend.service;

import com.example.kinobackend.model.Movie;
import com.example.kinobackend.model.Showing;
import com.example.kinobackend.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> getMoviesWithShowings() {

        return movieRepository.findAll();
    }

    public List<Movie> getUpcomingMoviesWithinThreeMonths() {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusMonths(3);


        return movieRepository.findMoviesWithinDateRange(startDate, endDate);
    }
    public List<Showing> getShowingsForMovie(int movieId) {
        return movieRepository.findById(movieId)
                .map(Movie::getShowings)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
    }

        public List<Movie> getAllMovies() {
            return movieRepository.findAll();
        }

        public Movie getMovieById(int id) {
            return movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie not found"));
        }

        public Movie createMovie(Movie movie) {
            return movieRepository.save(movie);
        }

        public void deleteMovie(int id) {
            movieRepository.deleteById(id);
        }
}
