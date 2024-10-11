package com.example.kinoxpbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
@NamedEntityGraph
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private Integer movieId; // Primary key



    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
    @JoinTable(
            name = "movie_actor",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private Set<Actor> actors;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
    @JoinTable(
            name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
    @JoinTable(
            name = "movie_director",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "director_id")
    )
    private Set<Director> directors;


    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "year", nullable = false)
    private String year;

    @Column(name = "released", nullable = false)
    private String released;

    @Column(name = "runtime", nullable = false)
    private String runtime;

    @Column(name = "poster", nullable = false)
    private String poster;

    @Column(name = "imdbRating", nullable = false)
    private String imdbRating;

    @Column(name = "imdbID", nullable = false)
    private String imdbID;

    public Movie(Integer id, String title, String year, String released, String runtime, String poster, String imdbRating, String imdbID) {
        this.movieId = id;
        this.title = title;
        this.year = year;
        this.released = released;
        this.runtime = runtime;
        this.poster = poster;
        this.imdbRating = imdbRating;
        this.imdbID = imdbID;
    }

    public Movie(String title, String year, String released, String runtime, String poster, String imdbRating, String imdbID,
                 Set<Actor> actors, Set<Director> directors, Set<Genre> genres) {
        this.title = title;
        this.year = year;
        this.released = released;
        this.runtime = runtime;
        this.poster = poster;
        this.imdbRating = imdbRating;
        this.imdbID = imdbID;
        this.actors = actors;          // Set the actors
        this.directors = directors;    // Set the directors
        this.genres = genres;          // Set the genres
    }



}