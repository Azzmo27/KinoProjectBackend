package com.example.backendkino;


import com.example.backendkino.model.Employee;
import com.example.backendkino.repository.EmployeeRepository;
import com.example.backendkino.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testVerifyEmployeeCredentials_Success() {

        String username = "testUser";
        String password = "testPassword";
        Employee employee = new Employee();
        employee.setUsername(username);
        employee.setPassword(password);

        when(employeeRepository.findByUsername(username)).thenReturn(Optional.of(employee));


        Optional<Employee> result = employeeService.verifyEmployeeCredentials(username, password);


        assertTrue(result.isPresent());
        assertEquals(employee, result.get());
    }

    @Test
    void testVerifyEmployeeCredentials_UserNotFound() {

        String username = "unknownUser";
        String password = "testPassword";

        when(employeeRepository.findByUsername(username)).thenReturn(Optional.empty());


        Optional<Employee> result = employeeService.verifyEmployeeCredentials(username, password);


        assertFalse(result.isPresent());
    }

    @Test
    void testVerifyEmployeeCredentials_IncorrectPassword() {

        String username = "testUser";
        String password = "incorrectPassword";
        Employee employee = new Employee();
        employee.setUsername(username);
        employee.setPassword("correctPassword");

        when(employeeRepository.findByUsername(username)).thenReturn(Optional.of(employee));


        Optional<Employee> result = employeeService.verifyEmployeeCredentials(username, password);


        assertFalse(result.isPresent());
    }
}
