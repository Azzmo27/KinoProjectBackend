package com.example.backendkino.service;
import com.example.backendkino.repository.ShowingRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class SeatService {
    private  ShowingRepository showingRepository;

    public SeatService(ShowingRepository showingRepository) {
        this.showingRepository = showingRepository;
    }

}