package com.example.zajecia7doktorki.service;

import com.example.zajecia7doktorki.command.AdminUpdateCommand;
import com.example.zajecia7doktorki.domain.*;
import com.example.zajecia7doktorki.model.Role;
import com.example.zajecia7doktorki.repository.ActionRepository;
import com.example.zajecia7doktorki.repository.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AdminServiceTest {
    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private AdminService adminService;
    @Mock
    private CustomerUserDetailsService customerUserDetailsService;
    @Mock
    private ActionRepository actionRepository;
    private UserDetails mockUserDetails;
    private Admin admin;
    private Patient patient;

    @BeforeEach
    void setUp() {
        mockUserDetails = mock(UserDetails.class);
        when(mockUserDetails.getUsername()).thenReturn("expectedUsername");
        when(customerUserDetailsService.getUserDetails()).thenReturn(mockUserDetails);

        setUpAdmin();
        setUpPatient();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void updateAdmin() {
        AdminUpdateCommand adminUpdateCommand = new AdminUpdateCommand();
        adminUpdateCommand.setName("Dr. Jane");
        adminUpdateCommand.setSurname("Doe");
        adminUpdateCommand.setAge(20);

        when(customerUserDetailsService.getUserDetails()).thenReturn(mockUserDetails);
        when(customerRepository.findByLogin("expectedUsername")).thenReturn(Optional.of(admin));
        when(customerRepository.save(any(Admin.class))).thenReturn(admin);

        Admin updatedAdmin = adminService.updateAdmin(adminUpdateCommand);

        assertThat(updatedAdmin.getName()).isEqualTo("Dr. Jane");
        assertThat(updatedAdmin.getSurname()).isEqualTo("Doe");
    }

    @Test
    void deleteCustomer() {
        Long customerId = 1L;
        Customer customer = new Customer();
        Admin admin = new Admin();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerUserDetailsService.getUserDetails()).thenReturn(admin);
        when(customerRepository.findByLogin("admin")).thenReturn(Optional.of(admin));

        adminService.deleteCustomer(customerId);

        verify(customerRepository).delete(customer);
        verify(actionRepository).save(any(Action.class));
    }

    @Test
    void deleteAppointment() {
    }

    @Test
    void setEnabled() {
    }

    @Test
    void setLocked() {
    }

    @Test
    void getActions() {
    }
    private void setUpAdmin() {
        admin = spy(new Admin());
        admin.setLogin("admin");
        admin.setPassword("password");
        admin.setPesel("12398745600");
    }
    private void setUpPatient() {
        patient = spy(new Patient());
        patient.setId(1L);
        patient.setName("Jan");
        patient.setSurname("Kowalski");
        patient.setAge(30);
        patient.setPesel("12345678901");
        patient.setLogin("jan");
        patient.setPassword("jan");
        patient.setRole(Role.USER);
    }
}