package com.example.zajecia7doktorki.service;

import com.example.zajecia7doktorki.domain.Admin;
import com.example.zajecia7doktorki.domain.Doctor;
import com.example.zajecia7doktorki.domain.Patient;
import com.example.zajecia7doktorki.exception.LoginAlreadyExistsException;
import com.example.zajecia7doktorki.exception.PeselAlreadyExistsException;
import com.example.zajecia7doktorki.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private RegisterService registerService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private Patient patient;
    private Doctor doctor;
    private Admin admin;

    @BeforeEach
    void setup() {
        setUpPatient();
        setUpDoctor();
        setUpAdmin();
    }


    @Test
    void shouldCreatePatient() {
        when(validationService.isLoginInDatabase(patient.getLogin())).thenReturn(false);
        when(validationService.isPeselInDatabase(patient.getPesel())).thenReturn(false);
        when(customerRepository.save(any(Patient.class))).thenReturn(patient);
        when(passwordEncoder.encode(patient.getPassword())).thenReturn("encodedPassword");

        Patient result = registerService.createPatient(patient);

        assertThat(result).isNotNull();
        assertThat(result.getLogin()).isEqualTo(patient.getLogin());
        verify(customerRepository).save(any(Patient.class));
    }

    @Test
    void shouldThrowLoginAlreadyExistsException() {
        when(validationService.isLoginInDatabase(patient.getLogin())).thenReturn(true);

        assertThrows(LoginAlreadyExistsException.class, () -> registerService.createPatient(patient));
    }

    @Test
    void shouldThrowPeselAlreadyExistsException() {
        when(validationService.isLoginInDatabase(patient.getLogin())).thenReturn(false);
        when(validationService.isPeselInDatabase(patient.getPesel())).thenReturn(true);

        assertThrows(PeselAlreadyExistsException.class, () -> registerService.createPatient(patient));
    }

    @Test
    void shouldCreateDoctor() {
        when(validationService.isLoginInDatabase(doctor.getLogin())).thenReturn(false);
        when(validationService.isPeselInDatabase(doctor.getPesel())).thenReturn(false);
        when(customerRepository.save(any(Doctor.class))).thenReturn(doctor);
        when(passwordEncoder.encode(doctor.getPassword())).thenReturn("encodedPassword");

        Doctor result = registerService.createDoctor(doctor);

        assertThat(result).isNotNull();
        assertThat(result.getLogin()).isEqualTo(doctor.getLogin());
        verify(customerRepository).save(any(Doctor.class));
    }

    @Test
    void shouldCreateAdmin() {
        when(validationService.isLoginInDatabase(admin.getLogin())).thenReturn(false);
        when(validationService.isPeselInDatabase(admin.getPesel())).thenReturn(false);
        when(customerRepository.save(any(Admin.class))).thenReturn(admin);
        when(passwordEncoder.encode(admin.getPassword())).thenReturn("encodedPassword");

        Admin result = registerService.createAdmin(admin);

        assertThat(result).isNotNull();
        assertThat(result.getLogin()).isEqualTo(admin.getLogin());
        verify(customerRepository).save(any(Admin.class));
    }

    private void setUpPatient() {
        patient = new Patient();
        patient.setLogin("newUser");
        patient.setPassword("password");
        patient.setPesel("12345678901");
    }

    private void setUpDoctor() {
        doctor = new Doctor();
        doctor.setLogin("newDoctor");
        doctor.setPassword("password");
        doctor.setPesel("98765432109");
    }

    private void setUpAdmin() {
        admin = new Admin();
        admin.setLogin("newAdmin");
        admin.setPassword("password");
        admin.setPesel("12398745600");
    }
}
