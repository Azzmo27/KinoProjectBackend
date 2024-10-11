package com.example.kinoxpbackend.service;

import com.example.kinoxpbackend.model.Admin;

import java.util.Optional;

public interface ApiServiceGetAdmins {
    Optional<Admin> authenticate(String username, String password);
}
