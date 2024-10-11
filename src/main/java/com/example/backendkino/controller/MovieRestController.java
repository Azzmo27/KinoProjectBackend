package com.example.backendkino.controller;

import com.example.backendkino.model.Actor;
import com.example.backendkino.model.Director;
import com.example.backendkino.model.Genre;
import com.example.backendkino.model.Movie;
import com.example.backendkino.repository.ActorRepository;
import com.example.backendkino.repository.DirectorRepository;
import com.example.backendkino.repository.GenreRepository;
import com.example.backendkino.repository.MovieRepository;
import com.example.backendkino.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@CrossOrigin
@RestController
public class MovieRestController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @GetMapping("/movies")
    public List<Movie> retrieveAllMovies() {
        return movieRepository.findAll();
    }

    @GetMapping("/getMovies")
    public List<Movie> fetchAllMovies() {
        return movieService.fetchAllMovies();
    }

    @GetMapping("/movie/{id}")
    public ResponseEntity<Movie> fetchMovieById(@PathVariable int id) {
        Optional<Movie> movieOptional = movieRepository.findById(id);
        return movieOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/movies")
    @ResponseStatus(HttpStatus.CREATED)
    public Movie createMovie(@RequestBody Movie movie) {
        Set<Actor> existingActors = new HashSet<>();
        Set<Director> existingDirectors = new HashSet<>();
        Set<Genre> existingGenres = new HashSet<>();

        for (Actor actor : movie.getActors()) {
            Actor existingActor = actorRepository.findActorByFullName(actor.getFullName());
            if (existingActor == null) {
                existingActor = new Actor(actor.getFullName());
            }
            existingActors.add(existingActor);
        }
        movie.setActors(existingActors);

        for (Director director : movie.getDirectors()) {
            Director existingDirector = directorRepository.findDirectorByFullName(director.getFullName());
            if (existingDirector == null) {
                existingDirector = new Director(director.getFullName());
            }
            existingDirectors.add(existingDirector);
        }
        movie.setDirectors(existingDirectors);

        for (Genre genre : movie.getGenres()) {
            Genre existingGenre = genreRepository.findByGenreName(genre.getGenreName());
            if (existingGenre == null) {
                existingGenre = new Genre(genre.getGenreName());
            }
            existingGenres.add(existingGenre);
        }
        movie.setGenres(existingGenres);

        return movieRepository.save(movie);
    }

    @PutMapping("/movie/{id}")
    public ResponseEntity<Movie> updateExistingMovie(@PathVariable int id, @RequestBody Movie movie) {
        if (!movieRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        movie.setMovieId(Integer.valueOf(id));
        Movie updatedMovie = movieRepository.save(movie);
        return ResponseEntity.ok(updatedMovie);
    }

    @DeleteMapping("/movie/{id}")
    public ResponseEntity<String> removeMovie(@PathVariable int id) {
        if (!movieRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"Movie not found\"}");
        }
        movieRepository.deleteById(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"message\": \"Movie deleted successfully\"}");
    }

    @GetMapping("/movies/search")
    public ResponseEntity<List<Movie>> searchMoviesByTitle(@RequestParam String title) {
        List<Movie> movies = movieRepository.findByTitleContaining(title);
        if (movies.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(movies);
        } else {
            return ResponseEntity.ok(movies);
        }
    }
}
