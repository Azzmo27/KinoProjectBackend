package com.example.kinoxpbackend.repository;

import com.example.kinoxpbackend.model.Booking;
import com.example.kinoxpbackend.model.Showing;
import com.example.kinoxpbackend.model.Booking;
import com.example.kinoxpbackend.model.Showing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface BookingRepository extends JpaRepository<Booking,Integer> {
Set<Booking> getBookingByShowing(Showing showing);
}
