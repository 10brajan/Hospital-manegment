package com.example.zajecia7doktorki.service;

import com.example.zajecia7doktorki.command.DoctorCommand;
import com.example.zajecia7doktorki.domain.Appointment;
import com.example.zajecia7doktorki.domain.Doctor;
import com.example.zajecia7doktorki.domain.Patient;
import com.example.zajecia7doktorki.exception.DoctorAlreadyExistsException;
import com.example.zajecia7doktorki.exception.DoctorNotFoundException;
import com.example.zajecia7doktorki.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DoctorServiceTest {
    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private DoctorService doctorService;
    @Mock
    private ValidationService validationService;

    private Doctor doctor;
    private Set<Patient> expectedPatients;
    private List<Appointment> appointments;

    @BeforeEach
    void setUp() {
        doctor = spy(new Doctor());
        doctor.setId(1L);
        doctor.setName("Jan");
        doctor.setSurname("Kowalski");
        doctor.setSpecialization("Kardiolog");
        doctor.setAge(28);
        doctor.setPesel("12345678901");

        Patient patient1 = new Patient();
        patient1.setId(1L);
        patient1.setName("Patient");
        patient1.setSurname("One");
        patient1.setPesel("12345678901");

        Patient patient2 = new Patient();
        patient2.setId(2L);
        patient2.setName("Patient");
        patient2.setSurname("Two");
        patient2.setPesel("12345678902");

        expectedPatients = new HashSet<>();
        expectedPatients.add(patient1);
        expectedPatients.add(patient2);

        Appointment appointment1 = new Appointment();
        appointment1.setPatient(patient1);

        Appointment appointment2 = new Appointment();
        appointment2.setPatient(patient2);

        appointments = List.of(appointment1, appointment2);
        doctor.setAppointments(appointments);
    }

    @Test
    void shouldGetDoctor() {
        // given
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(doctor));

        // when
        Doctor actual = doctorService.getDoctor(1L);

        // then
        assertThat(actual).isEqualTo(doctor);
    }

    @Test
    void shouldThrowWHenDoctorNotFound() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> doctorService.getDoctor(1L))
                .isInstanceOf(DoctorNotFoundException.class)
                .hasMessage("Doctor with this id does not exist");

    }

    @Test
    void shouldCreateDoctor() {
        // given
        when(customerRepository.findByPesel(any())).thenReturn(Optional.empty());
        when(customerRepository.save(any(Doctor.class))).thenReturn(doctor);

        // when
        Doctor actual = doctorService.createDoctor(doctor);

        // then
        assertThat(actual).isEqualTo(doctor);
    }

    @Test
    void shouldThrowWhenDoctorAlreadyExists() {
        when(doctorRepository.findByPesel(any())).thenReturn(Optional.of(doctor));
        when(validationService.isEntityInDatabase(any())).thenReturn(true);

        assertThatThrownBy(() -> doctorService.createDoctor(doctor))
                .isInstanceOf(DoctorAlreadyExistsException.class)
                .hasMessage("Doctor with this pesel already exists");
    }

    @Test
    void shouldUpdateDoctor() {
        // Given
        DoctorCommand doctorCommand = new DoctorCommand();
        doctorCommand.setName("Dr. Jane");
        doctorCommand.setSurname("Doe");
        doctorCommand.setAge(46);
        doctorCommand.setSpecialization("Neurology");

        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        // When
        Doctor updatedDoctor = doctorService.updateDoctor(1L, doctorCommand);

        // Then
        assertThat(updatedDoctor.getName()).isEqualTo(doctorCommand.getName());
        assertThat(updatedDoctor.getSurname()).isEqualTo(doctorCommand.getSurname());
        assertThat(updatedDoctor.getAge()).isEqualTo(doctorCommand.getAge());
        assertThat(updatedDoctor.getSpecialization()).isEqualTo(doctorCommand.getSpecialization());
    }

    @Test
    void shouldDeleteDoctor() {
        // Given
        when(doctorRepository.findById(any())).thenReturn(Optional.of(doctor));
        doNothing().when(doctorRepository).delete(any(Doctor.class));

        // When
        doctorService.deleteDoctor(1L);

        // Then
        verify(doctorRepository).delete(any(Doctor.class));
    }

    @Test
    void getDoctorPatients() {
        // Given
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));

        // When
        Set<Patient> patients = doctorService.getDoctorPatients(1L);

        // Then
        assertThat(patients)
                .hasSameSizeAs(expectedPatients)
                .hasSameElementsAs(expectedPatients);
    }
}