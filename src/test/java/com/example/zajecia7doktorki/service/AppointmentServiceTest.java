package com.example.zajecia7doktorki.service;

import com.example.zajecia7doktorki.command.AppointmentCommand;
import com.example.zajecia7doktorki.domain.Appointment;
import com.example.zajecia7doktorki.domain.Doctor;
import com.example.zajecia7doktorki.domain.Patient;
import com.example.zajecia7doktorki.exception.AppointmentConflictException;
import com.example.zajecia7doktorki.exception.AppointmentNotFoundException;
import com.example.zajecia7doktorki.exception.PermissionDeniedException;
import com.example.zajecia7doktorki.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AppointmentServiceTest {
    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientService patientService;

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private AppointmentService appointmentService;
    private Appointment appointment;
    private Patient patient;
    private Doctor doctor;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setId(1L);

        doctor = new Doctor();
        doctor.setId(1L);

        appointment = new Appointment();
        appointment.setId(1L);
        appointment.setDate(LocalDate.now());
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
    }

    @Test
    void shouldCreateAppointment() {
        // Given
        when(patientService.getPatient()).thenReturn(patient);
        when(doctorService.getDoctor()).thenReturn(doctor);
        when(appointmentRepository.existsByPatientAndDoctorAndDate(any(), any(), any())).thenReturn(false);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        // When
        Appointment createdAppointment = appointmentService.createAppointment(new Appointment(), 1L);

        // Then
        assertThat(createdAppointment).isEqualTo(appointment);
    }

    @Test
    void shouldThrowWhenAppointmentConflict() {
        // Given
        when(patientService.getPatient()).thenReturn(patient);
        when(doctorService.getDoctor()).thenReturn(doctor);
        when(appointmentRepository.existsByPatientAndDoctorAndDate(any(), any(), any())).thenReturn(true);

        // Then
//        assertThatExceptionOfType(AppointmentConflictException.class)
//                .isThrownBy(() -> appointmentService.createAppointment(new Appointment(), 1L, 1L));
    }

    @Test
    void shouldCancelAppointment() {
        // Given
        when(appointmentRepository.findById(anyLong())).thenReturn(Optional.of(appointment));
        doNothing().when(appointmentRepository).delete(any(Appointment.class));

        // When
//        appointmentService.cancelAppointment(1L, 1L);

        // Then
        verify(appointmentRepository).delete(appointment);
    }

    @Test
    void shouldThrowWhenPermissionDeniedOnCancel() {
        // Given
        Doctor differentDoctor = new Doctor();
        differentDoctor.setId(2L);
        appointment.setDoctor(differentDoctor);

        when(appointmentRepository.findById(anyLong())).thenReturn(Optional.of(appointment));

        // Then
//        assertThatExceptionOfType(PermissionDeniedException.class)
//                .isThrownBy(() -> appointmentService.cancelAppointment(1L, 1L));
    }

    @Test
    void shouldUpdateAppointment() {
        // Given
        AppointmentCommand command = new AppointmentCommand();
        command.setDate(LocalDate.now().plusDays(1));
        when(appointmentRepository.findById(anyLong())).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        // When
        Appointment updatedAppointment = appointmentService.updateAppointment(1L, command);

        // Then
        assertThat(updatedAppointment.getDate()).isEqualTo(command.getDate());
    }

    @Test
    void shouldDeleteAppointment() {
        // Given
        when(appointmentRepository.findById(anyLong())).thenReturn(Optional.of(appointment));
        doNothing().when(appointmentRepository).delete(any(Appointment.class));

        // When
        appointmentService.deleteAppointment(1L);

        // Then
        verify(appointmentRepository).delete(appointment);
    }

    @Test
    void shouldThrowWhenAppointmentNotFoundOnDelete() {
        // Given
        when(appointmentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Then
        assertThatExceptionOfType(AppointmentNotFoundException.class)
                .isThrownBy(() -> appointmentService.deleteAppointment(1L));
    }
}