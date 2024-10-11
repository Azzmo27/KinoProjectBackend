package com.example.backendkino.controller;

import com.example.backendkino.model.Employee;
import com.example.backendkino.repository.EmployeeRepository;
import com.example.backendkino.service.EmployeeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:63342", allowCredentials = "true")
@RestController
@RequestMapping("/employee")
public class EmployeeRestController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping("/register")
    public ResponseEntity<String> registerEmployee(@RequestBody Employee employee) {
        if (employeeRepository.findByUsername(employee.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already taken. Please choose another.");
        }
        employeeRepository.save(employee);
        return ResponseEntity.ok("Employee registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateEmployee(@RequestBody Employee loginRequest, HttpSession session) {
        Optional<Employee> employeeOptional = employeeService.verifyEmployeeCredentials(loginRequest.getUsername(), loginRequest.getPassword());

        if (employeeOptional.isPresent()) {
            session.setAttribute("employee", employeeOptional.get());
            return ResponseEntity.ok("Login successful!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid login credentials.");
        }
    }

    @GetMapping("/check-session")
    public ResponseEntity<String> verifyEmployeeSession(HttpSession session) {
        if (session.getAttribute("employee") != null) {
            return ResponseEntity.ok("Employee session is active.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No active employee session found.");
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logoutEmployee(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logout successful!");
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<?> fetchEmployeeById(@PathVariable int employeeId, HttpSession session) {
        Employee loggedInEmployee = (Employee) session.getAttribute("employee");

        if (loggedInEmployee == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to view employee details.");
        }

        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (!optionalEmployee.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found.");
        }

        return ResponseEntity.ok(optionalEmployee.get());
    }

    @GetMapping("/all")
    public ResponseEntity<?> listAllEmployees(HttpSession session) {
        Employee loggedInEmployee = (Employee) session.getAttribute("employee");

        if (loggedInEmployee == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to view the list of employees.");
        }

        List<Employee> employees = employeeRepository.findAll();
        return ResponseEntity.ok(employees);
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<?> modifyEmployeeDetails(@PathVariable int employeeId, @RequestBody Employee employeeDetails, HttpSession session) {
        Employee loggedInEmployee = (Employee) session.getAttribute("employee");

        if (loggedInEmployee == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to update employee details.");
        }

        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (!optionalEmployee.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found.");
        }

        Employee employeeToUpdate = optionalEmployee.get();
        employeeToUpdate.setUsername(employeeDetails.getUsername());
        employeeToUpdate.setFullName(employeeDetails.getFullName());

        if (employeeDetails.getPassword() != null && !employeeDetails.getPassword().isEmpty()) {
            employeeToUpdate.setPassword(employeeDetails.getPassword());
        }

        employeeRepository.save(employeeToUpdate);
        return ResponseEntity.ok(employeeToUpdate);
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<String> removeEmployee(@PathVariable int employeeId, HttpSession session) {
        Employee loggedInEmployee = (Employee) session.getAttribute("employee");

        if (loggedInEmployee == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to delete an employee.");
        }

        if (loggedInEmployee.getEmployeeId() == employeeId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot delete your own account.");
        }

        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (!optionalEmployee.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found.");
        }

        employeeRepository.deleteById(employeeId);
        return ResponseEntity.ok("Employee deleted successfully.");
    }
}
