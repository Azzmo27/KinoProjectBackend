package com.example.kinobackend.controller;

import com.example.kinobackend.model.Showing;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @PostMapping("/create-showing")
    public ResponseEntity<String> createShowing(@RequestBody Showing showing) {
        return ResponseEntity.ok("New showing created!");
    }
}


