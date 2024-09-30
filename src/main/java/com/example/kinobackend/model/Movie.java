package com.example.kinobackend.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String movieTitle;
    private String genre;
    private int ageLimit;

    @OneToMany(mappedBy = "movie",cascade = CascadeType.ALL)
    private List<Showing> showing;

    public Movie(){
    }
    public Movie(String movieTitle, String genre, int ageLimit, int id){
        this.movieTitle = movieTitle;
        this.genre = genre;
        this.ageLimit = ageLimit;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(int ageLimit) {
        this.ageLimit = ageLimit;
    }

    public List<Showing> getShowings() {
        return showing;
    }

    public void setShowings(List<Showing> showings) {
        this.showing = showings;
    }

}
