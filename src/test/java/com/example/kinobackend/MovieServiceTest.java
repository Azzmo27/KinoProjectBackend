package com.example.kinobackend;
import com.example.kinobackend.model.Movie;
import com.example.kinobackend.model.Showing;
import com.example.kinobackend.repository.MovieRepository;
import com.example.kinobackend.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

    public class MovieServiceTest {

        @Mock
        private MovieRepository movieRepository;

        @InjectMocks
        private MovieService movieService;

        @BeforeEach
        public void setUp() {
            MockitoAnnotations.openMocks(this);
        }

        @Test
        public void testGetMoviesWithShowings() {
            List<Movie> mockMovies = new ArrayList<>();
            mockMovies.add(new Movie());
            when(movieRepository.findAll()).thenReturn(mockMovies);

            List<Movie> result = movieService.getMoviesWithShowings();

            assertNotNull(result);
            assertEquals(1, result.size());
            verify(movieRepository, times(1)).findAll();
        }

        @Test
        public void testGetUpcomingMoviesWithinThreeMonths() {
            LocalDateTime startDate = LocalDateTime.now();
            LocalDateTime endDate = startDate.plusMonths(3);
            List<Movie> mockMovies = new ArrayList<>();
            when(movieRepository.findMoviesWithinDateRange(startDate, endDate)).thenReturn(mockMovies);

            List<Movie> result = movieService.getUpcomingMoviesWithinThreeMonths();

            assertNotNull(result);
            verify(movieRepository, times(1)).findMoviesWithinDateRange(startDate, endDate);
        }

        @Test
        public void testGetShowingsForMovie() {
            Movie movie = new Movie();
            List<Showing> showings = new ArrayList<>();
            movie.setShowings(showings);
            when(movieRepository.findById(1)).thenReturn(Optional.of(movie));

            List<Showing> result = movieService.getShowingsForMovie(1);

            assertNotNull(result);
            assertEquals(showings, result);
            verify(movieRepository, times(1)).findById(1);
        }

        @Test
        public void testGetMovieById() {
            Movie mockMovie = new Movie();
            when(movieRepository.findById(1)).thenReturn(Optional.of(mockMovie));

            Movie result = movieService.getMovieById(1);

            assertNotNull(result);
            assertEquals(mockMovie, result);
            verify(movieRepository, times(1)).findById(1);
        }

        @Test
        public void testCreateMovie() {
            Movie movie = new Movie();
            when(movieRepository.save(movie)).thenReturn(movie);

            Movie result = movieService.createMovie(movie);

            assertNotNull(result);
            verify(movieRepository, times(1)).save(movie);
        }

        @Test
        public void testDeleteMovie() {
            int movieId = 1;

            movieService.deleteMovie(movieId);

            verify(movieRepository, times(1)).deleteById(movieId);
        }
    }
