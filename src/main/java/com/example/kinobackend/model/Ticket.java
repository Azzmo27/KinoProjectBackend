package com.example.kinobackend.model;

import jakarta.persistence.*;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private double price;

    @ManyToOne
    @JoinColumn(name = "showing", referencedColumnName = "id")
    private Showing showing;

    @ManyToOne
    @JoinColumn(name = "customer", referencedColumnName = "id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "seat", referencedColumnName = "id")
    private Seat seat;

    private boolean is3D; // New field for 3D films
    private boolean isLongFilm; // New field for films longer than 170 minutes
    private String bookingReference; // Unique booking reference

    // Constructors, getters, and setters...

    public boolean is3D() {
        return is3D;
    }

    public void set3D(boolean is3D) {
        this.is3D = is3D;
    }

    public boolean isLongFilm() {
        return isLongFilm;
    }

    public void setLongFilm(boolean isLongFilm) {
        this.isLongFilm = isLongFilm;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    // Other getters and setters...
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Showing getShowing() {
        return showing;
    }

    public void setShowing(Showing showing) {
        this.showing = showing;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }
}
