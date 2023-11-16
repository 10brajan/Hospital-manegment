package com.example.zajecia7doktorki.service;

import com.example.zajecia7doktorki.command.PatientCommand;
import com.example.zajecia7doktorki.domain.Appointment;
import com.example.zajecia7doktorki.domain.Patient;
import com.example.zajecia7doktorki.exception.PatientAlreadyExistsException;
import com.example.zajecia7doktorki.exception.PatientNotFoundException;
import com.example.zajecia7doktorki.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

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
    private CustomerRepository patientRepository;

    @InjectMocks
    private PatientService patientService;
    @Mock
    private ValidationService validationService;
    @Mock
    private DoctorRepository doctorRepository;

    private Patient expectedPatient;

    @BeforeEach
    void setUp() {
        expectedPatient = spy(new Patient());
        expectedPatient.setId(1L);
        expectedPatient.setName("Jan");
        expectedPatient.setSurname("Kowalski");
        expectedPatient.setAge(30);
        expectedPatient.setPesel("12345678901");
        doReturn(Collections.emptyList()).when(expectedPatient).getAppointments();
    }

    @Test
    void shouldGetPatient() {
        // Given
        when(patientRepository.findById(any())).thenReturn(Optional.of(expectedPatient));

        // When
        Patient actual = patientService.getPatient(1L);

        // Then
        assertThat(actual).isEqualTo(expectedPatient);
    }

    @Test
    void shouldThrowWhenPatientNotFound() {
        // Given
        when(patientRepository.findById(any())).thenReturn(Optional.empty());

        // Then
        assertThatExceptionOfType(PatientNotFoundException.class)
                .isThrownBy(() -> patientService.getPatient(1L));
    }

    @Test
    void shouldCreatePatient() {
        // Given
//        when(patientRepository.findByPesel(any())).thenReturn(Optional.empty());
        when(patientRepository.save(any(Patient.class))).thenReturn(expectedPatient);

        // When
        Patient actual = patientService.createPatient(expectedPatient);

        // Then
        assertThat(actual).isEqualTo(expectedPatient);
    }

    @Test
    void shouldThrowWhenPatientAlreadyExists() {
        // Given
        when(patientRepository.findByPesel(any())).thenReturn(Optional.of(expectedPatient));
        when(validationService.isEntityInDatabase(any())).thenReturn(true);

        // Then
        assertThatExceptionOfType(PatientAlreadyExistsException.class)
                .isThrownBy(() -> patientService.createPatient(expectedPatient));
    }

    @Test
    void shouldUpdatePatient() {
        // Given
        when(patientRepository.findById(any())).thenReturn(Optional.of(expectedPatient));
        when(patientRepository.save(any(Patient.class))).thenReturn(expectedPatient);
        PatientCommand patientCommand = new PatientCommand();
        patientCommand.setName("Updated Name");

        // When
        Patient updatedPatient = patientService.updatePatient(1L, patientCommand);

        // Then
        assertThat(updatedPatient.getName()).isEqualTo("Updated Name");
    }

    @Test
    void shouldDeletePatient() {
        // Given
        when(patientRepository.findById(any())).thenReturn(Optional.of(expectedPatient));
        doNothing().when(patientRepository).delete(any(Patient.class));

        // When
        patientService.deletePatient(1L);

        // Then
        verify(patientRepository).delete(any(Patient.class));
    }

    @Test
    void shouldGetPatientAppointments() {
        // Given
        when(patientRepository.findById(any())).thenReturn(Optional.of(expectedPatient));
        when(expectedPatient.getAppointments()).thenReturn(Collections.emptyList());

        // When
        List<Appointment> appointments = patientService.getPatientAppointments(1L);

        // Then
        assertThat(appointments).isEmpty();
    }
}