package com.example.backendkino.service;

import com.example.backendkino.model.Employee;
import com.example.backendkino.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Optional<Employee> verifyEmployeeCredentials(String username, String password) {
        Optional<Employee> optionalEmployee = employeeRepository.findByUsername(username);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            if (employee.getPassword().equals(password)) {
                return Optional.of(employee);
            }
        }
        return Optional.empty();
    }
}