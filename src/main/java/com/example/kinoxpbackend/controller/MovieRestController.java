package com.example.kinoxpbackend.controller;


import com.example.kinoxpbackend.model.Actor;
import com.example.kinoxpbackend.model.Director;
import com.example.kinoxpbackend.model.Genre;
import com.example.kinoxpbackend.model.Movie;
import com.example.kinoxpbackend.repository.ActorRepository;
import com.example.kinoxpbackend.repository.DirectorRepository;
import com.example.kinoxpbackend.repository.GenreRepository;
import com.example.kinoxpbackend.repository.MovieRepository;
import com.example.kinoxpbackend.service.ApiServiceGetMovies;
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
    private ApiServiceGetMovies apiServiceGetMovies;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    ActorRepository actorRepository;
    @Autowired
    DirectorRepository directorRepository;
    @Autowired
    GenreRepository genreRepository;
    @GetMapping("/getMovies")
    public List<Movie> getMovies() {
        return apiServiceGetMovies.getMovies();
    }
    @GetMapping ("/movies")
    public List<Movie> getAllMovies(){
        return movieRepository.findAll();
    }
    @GetMapping ("/movie/{id}")
    public ResponseEntity<Movie> getMovieById (@PathVariable int id){
        Optional<Movie> movieOptional = movieRepository.findById(id);
        return movieOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping ("/movie/{id}")
    public ResponseEntity<Movie> updateMovie (@PathVariable int id, @RequestBody Movie movie){
        if (!movieRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        movie.setMovieId(Integer.valueOf(id));
        Movie updatedMovie = movieRepository.save(movie);
        return ResponseEntity.ok(updatedMovie);

    }
//    @GetMapping("/{movieId}")
//    public ResponseEntity<Movie> getMovieById(@PathVariable int movieId) {
//        Movie movie = movieRepository.findById(movieId)
//                .orElseThrow(() -> new EntityNotFoundException("Movie not found with id: " + movieId));
//        return ResponseEntity.ok(movie);
//    }

/*

        kommune.setKode(kode);
        Kommune updatedKommune = kommuneRepository.save(kommune);
        return ResponseEntity.ok(updatedKommune);
    }*/


    @PostMapping("/movies")
    @ResponseStatus(HttpStatus.CREATED)
    public Movie addMovie(@RequestBody Movie movie) {
        // Initialize empty sets to hold existing entities
        Set<Actor> existingActors = new HashSet<>();
        Set<Director> existingDirectors = new HashSet<>();
        Set<Genre> existingGenres = new HashSet<>();

        // Process actors
        for (Actor actor : movie.getActors()) {
            Actor existingActor = actorRepository.findActorByFullName(actor.getFullName());
            if (existingActor == null) {
                existingActor = new Actor(actor.getFullName());
            }
            existingActors.add(existingActor);
        }
        movie.setActors(existingActors);

        // Process directors
        for (Director director : movie.getDirectors()) {
            Director existingDirector = directorRepository.findDirectorByFullName(director.getFullName());
            if (existingDirector == null) {
                existingDirector = new Director(director.getFullName());
            }
            existingDirectors.add(existingDirector);
        }
        movie.setDirectors(existingDirectors);

        // Process genres
        for (Genre genre : movie.getGenres()) {
            Genre existingGenre = genreRepository.findByGenreName(genre.getGenreName());
            if (existingGenre == null) {
                existingGenre = new Genre(genre.getGenreName());
            }
            existingGenres.add(existingGenre);
        }
        movie.setGenres(existingGenres);

        // Save the movie entity
        return movieRepository.save(movie);
    }



    @DeleteMapping("/movie/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable int id) {
        System.out.println("Attempting to delete movie with ID: " + id);
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
