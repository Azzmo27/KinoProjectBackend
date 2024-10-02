package com.example.kinobackend.service;

import com.example.kinobackend.model.Customer;
import com.example.kinobackend.model.Seat;
import com.example.kinobackend.model.Showing;
import com.example.kinobackend.model.Ticket;
import com.example.kinobackend.repository.TicketRepository; // Import your repository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ReservationService {

    private final TicketRepository ticketRepository;

    @Autowired
    public ReservationService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public Ticket reserveTicket(Showing showing, List<Seat> seats, Customer customer) {
        if (seats.isEmpty()) {
            throw new IllegalArgumentException("No seats selected for reservation");
        }

        Ticket ticket = new Ticket();
        ticket.setShowing(showing);
        ticket.setCustomer(customer);
        ticket.setSeat(seats.get(0));

        double totalPrice = 0;
        int ticketCount = seats.size();


        for (Seat seat : seats) {
            double basePrice = calculateBasePrice(seat);
            totalPrice += basePrice;


            if (showing.getMovie().is3D()) {
                totalPrice += 1.50;
                ticket.set3D(true);
            }
            if (showing.getMovie().getDuration() > 170) {
                totalPrice += 2.00;
                ticket.setLongFilm(true);
            }
        }


        if (ticketCount <= 5) {
            totalPrice += 5.00;
        } else if (ticketCount > 10) {
            totalPrice *= 0.93;
        }

        ticket.setPrice(totalPrice);


        ticket.setBookingReference(UUID.randomUUID().toString());


        for (Seat seat : seats) {
            if (!seat.isAvailable()) {
                throw new IllegalStateException("En eller flere er allerede reserveret");
            }
            seat.setAvailable(false);
        }

        // Save ticket to the database
        ticketRepository.save(ticket);

        return ticket;
    }


    private double calculateBasePrice(Seat seat) {
        if (seat.isCowboy()) {
            return 5.00;
        } else if (seat.isSofa()) {
            return 12.00;
        } else {
            return 10.00;
        }
    }
}
