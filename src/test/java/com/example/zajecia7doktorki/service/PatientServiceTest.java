package com.example.zajecia7doktorki.service;

import com.example.zajecia7doktorki.command.PatientUpdateCommand;
import com.example.zajecia7doktorki.domain.Appointment;
import com.example.zajecia7doktorki.domain.Customer;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
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
    void shouldGetAllPatients() {
        Patient patient1 = new Patient();
        patient1.setName("Jan");
        patient1.setSurname("Kowalski");
        // ... (ustaw pozostałe pola dla patient1)

        Patient patient2 = new Patient();
        patient2.setName("Anna");
        patient2.setSurname("Nowak");
        // ... (ustaw pozostałe pola dla patient2)

        List<Customer> expectedPatientsList = List.of(patient1, patient2);
        Page<Customer> expectedPatients = new PageImpl<>(expectedPatientsList);

        when(customerRepository.findAllByUserType("PATIENT", PageRequest.of(0, 20)))
                .thenReturn(expectedPatients);

        Page<Patient> actualPatients = patientService.getAllPatients(PageRequest.of(0, 20));

        assertThat(actualPatients.getTotalElements()).isEqualTo(2);
        assertThat(actualPatients.getContent()).isEqualTo(expectedPatientsList);
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

//    @Test
//    void shouldGetPatientAppointments() {
//        when(customerUserDetailsService.getUserDetails()).thenReturn(mockUserDetails);
//        when(customerRepository.findByLogin("expectedUsername")).thenReturn(Optional.of(expectedPatient));
//
//        List<Appointment> appointmentsList = List.of(/* utwórz i dodaj obiekty Appointment */);
//        Page<Appointment> appointmentsPage = new PageImpl<>(appointmentsList, PageRequest.of(0, 10), appointmentsList.size());
//
//        // Zakładam, że masz metodę w Patient, która zwraca stronicowane wizyty
//        when(expectedPatient.getAppointments(PageRequest.of(0, 10))).thenReturn(appointmentsPage);
//
//        Page<Appointment> actualAppointments = patientService.getPatientAppointments(PageRequest.of(0, 10));
//
//        assertThat(actualAppointments.getTotalElements()).isEqualTo(appointmentsList.size());
//        assertThat(actualAppointments.getContent()).containsExactlyElementsOf(appointmentsList);
//    }

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