package com.example.kinobackend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "showing")
public class Showing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "movie_id", referencedColumnName = "id")

    private Movie movie;

    private LocalDateTime showingTime;
    private int theaterNumber;

    @OneToMany(mappedBy = "showing", cascade = CascadeType.ALL)
    private List<Seat> seats;

    public Showing() {}

    public Showing(Movie movie, LocalDateTime showingTime, int theaterNumber) {
        this.movie = movie;
        this.showingTime = showingTime;
        this.theaterNumber = theaterNumber;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public LocalDateTime getShowingTime() {
        return showingTime;
    }

    public void setShowingTime(LocalDateTime showingTime) {
        this.showingTime = showingTime;
    }

    public int getTheaterNumber() {
        return theaterNumber;
    }

    public void setTheaterNumber(int theaterNumber) {
        this.theaterNumber = theaterNumber;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public List<Seat> getAvailableSeats() {
        return seats.stream()
                .filter(seat -> !seat.isReserved())
                .collect(Collectors.toList());
    }


}

