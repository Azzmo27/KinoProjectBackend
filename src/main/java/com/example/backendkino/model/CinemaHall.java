package com.example.backendkino.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cinemaHall")
public class CinemaHall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cinemaHall_id")
    private int cinemaHallId;

    @Column(name = "seatRows", nullable = false)
    private int seatRows;

    @Column(name = "seatsPerRow", nullable = false)
    private int seatsPerRow;


}