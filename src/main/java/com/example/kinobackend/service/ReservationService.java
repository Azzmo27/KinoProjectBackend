package com.example.kinobackend.service;

import com.example.kinobackend.model.Customer;
import com.example.kinobackend.model.Seat;
import com.example.kinobackend.model.Showing;
import com.example.kinobackend.model.Ticket;
import com.example.kinobackend.repository.TicketRepository; // Import your repository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReservationService {

    private final TicketRepository ticketRepository; // Inject your Ticket repository

    @Autowired
    public ReservationService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket reserveTicket(Showing showing, List<Seat> seats, Customer customer) {
        Ticket ticket = new Ticket();
        ticket.setShowing(showing);
        ticket.setCustomer(customer);
        ticket.setSeat(seats.get(0)); // Assuming the first seat is the main one for pricing.

        double totalPrice = 0;
        int ticketCount = seats.size();

        // Calculate total price based on seat type and movie specifications
        for (Seat seat : seats) {
            double basePrice = calculateBasePrice(seat);
            totalPrice += basePrice;

            // Check for additional charges
            if (showing.getMovie().is3D()) {
                totalPrice += 1.50; // Additional fee for 3D
                ticket.set3D(true);
            }
            if (showing.getMovie().getDuration() > 170) {
                totalPrice += 2.00; // Additional fee for long films
                ticket.setLongFilm(true);
            }
        }

        // Apply reservation fees or discounts
        if (ticketCount <= 5) {
            totalPrice += 5.00; // Reservation fee for <=5 tickets
        } else if (ticketCount > 10) {
            totalPrice *= 0.93; // 7% discount for >10 tickets
        }

        ticket.setPrice(totalPrice);

        // Generate unique booking reference
        ticket.setBookingReference(UUID.randomUUID().toString());

        // Mark reserved seats as unavailable
        for (Seat seat : seats) {
            seat.setAvailable(false); // Assuming there's an 'available' field
        }

        // Save ticket to the database
        ticketRepository.save(ticket);

        return ticket; // Return the ticket with confirmation details
    }

    private double calculateBasePrice(Seat seat) {
        if (seat.isCowboy()) {
            return 5.00; // Example price for cowboy seat
        } else if (seat.isSofa()) {
            return 12.00; // Example price for sofa seat
        } else {
            return 10.00; // Default price for regular seats
        }
    }
}
