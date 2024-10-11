package com.example.kinoxpbackend.service;

import com.example.kinoxpbackend.model.Admin;
import com.example.kinoxpbackend.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApiServiceGetAdminsImpl implements ApiServiceGetAdmins {
    @Autowired
    AdminRepository adminRepository;

@Override
    public Optional<Admin> authenticate(String username, String password){
        Optional<Admin> optionalAdmin = adminRepository.findByUsername(username);
        if (optionalAdmin.isPresent()){
            Admin admin = optionalAdmin.get();
            if (admin.getPassword().equals(password)){
                return Optional.of(admin);
            }
        }
        return Optional.empty();
    }


}
