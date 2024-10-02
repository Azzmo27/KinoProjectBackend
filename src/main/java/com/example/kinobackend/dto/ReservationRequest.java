package com.example.kinobackend.dto;

import com.example.kinobackend.model.Customer;
import com.example.kinobackend.model.Seat;
import com.example.kinobackend.model.Showing;

import java.util.List;

public class ReservationRequest {
    private Showing showing;
    private List<Seat> seats;
    private Customer customer;


    public Showing getShowing() {
        return showing;
    }

    public void setShowing(Showing showing) {
        this.showing = showing;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
