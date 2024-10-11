package com.example.kinoxpbackend.controller;

import com.example.kinoxpbackend.model.Admin;
import com.example.kinoxpbackend.repository.AdminRepository;
import com.example.kinoxpbackend.service.ApiServiceGetAdmins;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:63342", allowCredentials = "true")
@RestController
@RequestMapping("/admin")
public class AdminRestController {

    @Autowired
    ApiServiceGetAdmins apiServiceGetAdmins;
    @Autowired
    AdminRepository adminRepository;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Admin loginRequest, HttpSession httpSession) {
        Optional<Admin> adminOptional = apiServiceGetAdmins.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        if (adminOptional.isPresent()) {
            httpSession.setAttribute("admin", adminOptional.get());
            return ResponseEntity.ok("Login succesfull");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid login request");
        }
    }

    @GetMapping("/check-admin-presence")
    public ResponseEntity<String> checkForAdmin(HttpSession httpSession) {
        if (httpSession.getAttribute("admin") != null) {
            return ResponseEntity.ok("Admin session active");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No active admin session");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Admin admin) {
        if (adminRepository.findByUsername(admin.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }
        adminRepository.save(admin);
        return ResponseEntity.ok("Admin registered successfully");
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logout successful");
    }

    @PutMapping("/{adminId}")
    public ResponseEntity<?> updateAdmin(@PathVariable int adminId, @RequestBody Admin adminDetails, HttpSession session) {

        Admin loggedInAdmin = (Admin) session.getAttribute("admin");
        if (loggedInAdmin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to update an admin.");
        }

        Optional<Admin> optionalAdmin = adminRepository.findById(adminId);
        if (!optionalAdmin.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found.");
        }

        Admin adminToUpdate = optionalAdmin.get();
        adminToUpdate.setUsername(adminDetails.getUsername());
        adminToUpdate.setFullName(adminDetails.getFullName());
        if (adminDetails.getPassword() != null && !adminDetails.getPassword().isEmpty()) {
            adminToUpdate.setPassword(adminDetails.getPassword());
        }

        adminRepository.save(adminToUpdate);
        return ResponseEntity.ok(adminToUpdate);
    }

    @DeleteMapping("/{adminId}")
    public ResponseEntity<String> deleteAdmin(@PathVariable int adminId, HttpSession session) {

        Admin loggedInAdmin = (Admin) session.getAttribute("admin");
        if (loggedInAdmin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to delete an admin.");
        }

        if (loggedInAdmin.getAdminId() == adminId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot delete your own account.");
        }

        Optional<Admin> optionalAdmin = adminRepository.findById(adminId);
        if (!optionalAdmin.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found.");
        }

        adminRepository.deleteById(adminId);
        return ResponseEntity.ok("Admin deleted successfully.");
    }

    @GetMapping("/{adminId}")
    public ResponseEntity<?> getAdminById(@PathVariable int adminId, HttpSession session) {

        Admin loggedInAdmin = (Admin) session.getAttribute("admin");
        if (loggedInAdmin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to view admin details.");
        }

        Optional<Admin> optionalAdmin = adminRepository.findById(adminId);
        if (!optionalAdmin.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found.");
        }

        return ResponseEntity.ok(optionalAdmin.get());
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllAdmins(HttpSession session) {

        Admin loggedInAdmin = (Admin) session.getAttribute("admin");
        if (loggedInAdmin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to view admins.");
        }

        List<Admin> admins = adminRepository.findAll();
        return ResponseEntity.ok(admins);
    }
}


