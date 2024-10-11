package com.example.kinoxpbackend.controller;

import com.example.kinoxpbackend.model.Theatre;
import com.example.kinoxpbackend.service.TheatreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/theatre")
@CrossOrigin
public class TheatreRestController {

    @Autowired
    private TheatreService theatreService;

    @PostMapping("/create")
    public ResponseEntity<?> createTheatre(@RequestBody Theatre theatre) {
        try {

            Theatre createdTheatre = theatreService.createTheatre(theatre.getSeatRows(), theatre.getSeatsPerRow());


            return ResponseEntity.status(HttpStatus.CREATED).body(createdTheatre);

        } catch (IllegalArgumentException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while creating the theatre.");
        }
    }
}
