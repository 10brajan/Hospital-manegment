package com.example.zajecia7doktorki.service;

import com.example.zajecia7doktorki.command.PatientUpdateCommand;
import com.example.zajecia7doktorki.domain.Appointment;
import com.example.zajecia7doktorki.domain.Patient;
import com.example.zajecia7doktorki.exception.PatientNotFoundException;
import com.example.zajecia7doktorki.model.Role;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
//import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PatientServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private PatientService patientService;
    @Mock
    private ValidationService validationService;

    @Mock
    private CustomerUserDetailsService customerUserDetailsService;
    private UserDetails mockUserDetails;

    private Patient expectedPatient;

    @BeforeEach
    void setUp() {
        mockUserDetails = mock(UserDetails.class);
        when(mockUserDetails.getUsername()).thenReturn("expectedUsername");
        when(customerUserDetailsService.getUserDetails()).thenReturn(mockUserDetails);

        setUpPatient();
        doReturn(Collections.emptyList()).when(expectedPatient).getAppointments();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldGetPatient() {

        when(customerRepository.findByLogin("expectedUsername")).thenReturn(Optional.of(expectedPatient));

        Patient actual = patientService.getPatient();

        assertThat(actual).isEqualTo(expectedPatient);
    }

    @Test
    void shouldThrowWhenPatientNotFound() {
        when(customerUserDetailsService.getUserDetails()).thenReturn(mockUserDetails);
        when(customerRepository.findByLogin("expectedUsername")).thenReturn(Optional.empty());

        assertThatExceptionOfType(PatientNotFoundException.class)
                .isThrownBy(() -> patientService.getPatient());
    }


    @Test
    void shouldUpdatePatient() {
        when(customerUserDetailsService.getUserDetails()).thenReturn(mockUserDetails);
        when(customerRepository.findByLogin("expectedUsername")).thenReturn(Optional.of(expectedPatient));
        when(customerRepository.save(any(Patient.class))).thenReturn(expectedPatient);

        PatientUpdateCommand patientUpdateCommand = new PatientUpdateCommand();
        patientUpdateCommand.setName("Updated Name");

        Patient updatedPatient = patientService.updatePatient(patientUpdateCommand);

        assertThat(updatedPatient.getName()).isEqualTo("Updated Name");
    }

    @Test
    void shouldDeletePatient() {
        when(customerUserDetailsService.getUserDetails()).thenReturn(mockUserDetails);
        when(customerRepository.findByLogin("expectedUsername")).thenReturn(Optional.of(expectedPatient));
        doNothing().when(customerRepository).delete(any(Patient.class));

        patientService.deletePatient();

        verify(customerRepository).delete(any(Patient.class));
    }

    @Test
    void shouldGetPatientAppointments() {
        when(customerUserDetailsService.getUserDetails()).thenReturn(mockUserDetails);
        when(customerRepository.findByLogin("expectedUsername")).thenReturn(Optional.of(expectedPatient));
        when(expectedPatient.getAppointments()).thenReturn(Collections.emptyList());

        List<Appointment> appointments = patientService.getPatientAppointments();

        assertThat(appointments).isEmpty();
    }

    private void setUpPatient() {
        expectedPatient = spy(new Patient());
        expectedPatient.setName("Jan");
        expectedPatient.setSurname("Kowalski");
        expectedPatient.setAge(30);
        expectedPatient.setPesel("12345678901");
        expectedPatient.setLogin("jan");
        expectedPatient.setPassword("jan");
        expectedPatient.setRole(Role.USER);
    }
}