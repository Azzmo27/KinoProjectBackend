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
    private boolean is3D;
    private int duration;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Showing> showings;

    public Movie() {
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

    public void setIs3D(boolean is3D) {
        this.is3D = is3D;
    }


    public boolean is3D() {
        return is3D;
    }

    public void set3D(boolean is3D) {
        this.is3D = is3D;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
    public List<Showing> getShowings() {
        return showings;
    }

    public void setShowings(List<Showing> showings) {
        this.showings = showings;
    }
}
