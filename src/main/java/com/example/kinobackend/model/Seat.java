package com.example.kinobackend.model;

import jakarta.persistence.*;

@Entity
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String row;
    private int seatNumber;
    private boolean isSofa;
    private boolean isCowboy;
    private boolean reserved; // Field to track if the seat is reserved
    private boolean available; // New field to track if the seat is available
    private double priceAdjustment;

    // Constructors
    public Seat() {
        // Default constructor
        this.reserved = false; // Initialize reserved to false
        this.available = true; // Initialize available to true
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public boolean isSofa() {
        return isSofa;
    }

    public void setSofa(boolean sofa) {
        this.isSofa = sofa;
    }

    public boolean isCowboy() {
        return isCowboy;
    }

    public void setCowboy(boolean cowboy) {
        this.isCowboy = cowboy;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
        this.available = !reserved; // Update availability based on reservation status
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public double getPriceAdjustment() {
        return priceAdjustment;
    }

    public void setPriceAdjustment(double priceAdjustment) {
        this.priceAdjustment = priceAdjustment;
    }
}
