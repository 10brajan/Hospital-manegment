package com.example.zajecia7doktorki.service;

import com.example.zajecia7doktorki.command.DoctorCommand;
import com.example.zajecia7doktorki.command.DoctorUpdateCommand;
import com.example.zajecia7doktorki.domain.Appointment;
import com.example.zajecia7doktorki.domain.Doctor;
import com.example.zajecia7doktorki.domain.Patient;
import com.example.zajecia7doktorki.exception.DoctorAlreadyExistsException;
import com.example.zajecia7doktorki.exception.DoctorNotFoundException;
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

import java.util.*;

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
    @Mock
    private CustomerUserDetailsService customerUserDetailsService;
    private UserDetails mockUserDetails;

    private Doctor doctor;
    private Set<Patient> expectedPatients;
    private List<Appointment> appointments;

    @BeforeEach
    void setUp() {
        mockUserDetails = mock(UserDetails.class);
        when(mockUserDetails.getUsername()).thenReturn("expectedUsername");
        when(customerUserDetailsService.getUserDetails()).thenReturn(mockUserDetails);

        setUpDoctor();
        doReturn(Collections.emptyList()).when(doctor).getAppointments();

//        doctor = spy(new Doctor());
//        doctor.setId(1L);
//        doctor.setName("Jan");
//        doctor.setSurname("Kowalski");
//        doctor.setSpecialization("Kardiolog");
//        doctor.setAge(28);
//        doctor.setPesel("12345678901");
//
//        Patient patient1 = new Patient();
//        patient1.setId(1L);
//        patient1.setName("Patient");
//        patient1.setSurname("One");
//        patient1.setPesel("12345678901");
//
//        Patient patient2 = new Patient();
//        patient2.setId(2L);
//        patient2.setName("Patient");
//        patient2.setSurname("Two");
//        patient2.setPesel("12345678902");
//
//        expectedPatients = new HashSet<>();
//        expectedPatients.add(patient1);
//        expectedPatients.add(patient2);
//
//        Appointment appointment1 = new Appointment();
//        appointment1.setPatient(patient1);
//
//        Appointment appointment2 = new Appointment();
//        appointment2.setPatient(patient2);
//
//        appointments = List.of(appointment1, appointment2);
//        doctor.setAppointments(appointments);
    }
    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldGetDoctor() {
        // given
        when(customerRepository.findByLogin("expectedUsername")).thenReturn(Optional.of(doctor));

        // when
        Doctor actual = doctorService.getDoctor();

        // then
        assertThat(actual).isEqualTo(doctor);
    }

    @Test
    void shouldGetAllDoctors() {
        Doctor doctor1 = new Doctor();
        doctor1.setName("brajan");
        doctor1.setSurname("szymanski");
        Doctor doctor2 = new Doctor();
        doctor2.setName("patryk");
    }

    @Test
    void shouldThrowWHenDoctorNotFound() {
        when(customerUserDetailsService.getUserDetails()).thenReturn(mockUserDetails);
        when(customerRepository.findByLogin("expectedUsername")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> doctorService.getDoctor())
                .isInstanceOf(DoctorNotFoundException.class)
                .hasMessage("Doctor with this login does not exist");

    }

    @Test
    void shouldUpdateDoctor() {
        DoctorUpdateCommand doctorUpdateCommand = new DoctorUpdateCommand();
        doctorUpdateCommand.setName("Dr. Jane");
        doctorUpdateCommand.setSurname("Doe");
        doctorUpdateCommand.setSpecialization("Neurology");

        when(customerUserDetailsService.getUserDetails()).thenReturn(mockUserDetails);
        when(customerRepository.findByLogin("expectedUsername")).thenReturn(Optional.of(doctor));
        when(customerRepository.save(any(Doctor.class))).thenReturn(doctor);

        Doctor updatedDoctor = doctorService.updateDoctor(doctorUpdateCommand);

        assertThat(updatedDoctor.getName()).isEqualTo("Dr. Jane");
        assertThat(updatedDoctor.getSurname()).isEqualTo("Doe");
    }

    @Test
    void shouldDeleteDoctor() {
        when(customerUserDetailsService.getUserDetails()).thenReturn(mockUserDetails);
        when(customerRepository.findByLogin("expectedUsername")).thenReturn(Optional.of(doctor));
        doNothing().when(customerRepository).delete(any(Doctor.class));

        doctorService.deleteDoctor();

        verify(customerRepository).delete(any(Doctor.class));
    }

    @Test
    void getDoctorPatients() {
        // Given
//        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));

        // When
//        Set<Patient> patients = doctorService.getDoctorPatients(1L);

        // Then
//        assertThat(patients)
//                .hasSameSizeAs(expectedPatients)
//                .hasSameElementsAs(expectedPatients);
        when(customerUserDetailsService.getUserDetails()).thenReturn(mockUserDetails);
        when(customerRepository.findByLogin("expectedUsername")).thenReturn(Optional.of(doctor));
        when(doctorService.getDoctorPatients()).thenReturn(expectedPatients);

        Set<Patient> patients = doctorService.getDoctorPatients();


        assertThat(patients)
                .hasSameSizeAs(expectedPatients)
                .hasSameElementsAs(expectedPatients);
    }
//    @Test
//    void shouldGetDoctorAppointments() {
//        when(customerUserDetailsService.getUserDetails()).thenReturn(mockUserDetails);
//        when(customerRepository.findByLogin("expectedUsername")).thenReturn(Optional.of(doctor));
//        when(doctor.getAppointments()).thenReturn(Collections.emptyList());
//
//        List<Appointment> appointments = doctorService.getDoctorAppointments();
//
//        assertThat(appointments).isEmpty();
//    }
    private void setUpDoctor() {
        doctor = spy(new Doctor());
        doctor.setName("Jan");
        doctor.setSurname("Kowalski");
        doctor.setSpecialization("Kardiolog");
        doctor.setAge(30);
        doctor.setPesel("12345678901");
        doctor.setLogin("jan");
        doctor.setPassword("jan");
        doctor.setRole(Role.USER);
    }
}