package com.example.zajecia7doktorki.controller;

import com.example.zajecia7doktorki.command.DoctorCommand;
import com.example.zajecia7doktorki.domain.Appointment;
import com.example.zajecia7doktorki.domain.Doctor;
import com.example.zajecia7doktorki.domain.Patient;
import com.example.zajecia7doktorki.repository.AppointmentRepository;
import com.example.zajecia7doktorki.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("application-tests")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class DoctorControllerTest {
    //    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .build();
        customerRepository.deleteAll();
    }

    @Test
    void shouldCreateDoctor() throws Exception {
        DoctorCommand doctorCommand = new DoctorCommand("Jan", "Kowalski", "Cardiology", 45, "98012112345", "Jan", "Jan");

        this.mockMvc.perform(post("/api/v1/doctors")
                        .content(objectMapper.writeValueAsString(doctorCommand))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Jan"))
                .andExpect(jsonPath("$.specialization").value("Cardiology"));
    }

    @Test
    void shouldFindDoctorById() throws Exception {
        Doctor doctor = createTestDoctor("Anna", "Nowak", 39, "78050512345", "Pediatrics");

        this.mockMvc.perform(get("/api/v1/doctors/{id}", doctor.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Anna"))
                .andExpect(jsonPath("$.specialization").value("Pediatrics"));
    }

    @Test
    void shouldGetAllDoctors() throws Exception {
        createTestDoctor("Anna", "Nowak", 39, "78050512345", "Pediatrics");
        createTestDoctor("Jan", "Kowalski", 45, "98012112345", "Cardiology");

        this.mockMvc.perform(get("/api/v1/doctors")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Anna"))
                .andExpect(jsonPath("$[1].name").value("Jan"));
    }

    @Test
    void shouldUpdateDoctor() throws Exception {
        Doctor doctor = createTestDoctor("Jan", "Kowalski", 45, "98012112345", "Cardiology");
        DoctorCommand doctorCommand = new DoctorCommand("Amelia", "Kowalska", "Dentist", 35, "11111111111", "Amelia", "Amelia");


        this.mockMvc.perform(put("/api/v1/doctors/{id}", doctor.getId())
                        .content(objectMapper.writeValueAsString(doctorCommand))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.surname").value("Nowak"))
                .andExpect(jsonPath("$.age").value(46))
                .andExpect(jsonPath("$.specialization").value("General Medicine"));
    }

    @Test
    void shouldDeleteDoctor() throws Exception {
        Doctor doctor = createTestDoctor("Jan", "Kowalski", 45, "98012112345", "Cardiology");

        this.mockMvc.perform(delete("/api/v1/doctors/{id}", doctor.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        assertTrue(customerRepository.findById(doctor.getId()).isEmpty());
    }

    @Test
    void shouldCancelAppointment() throws Exception {
        Doctor doctor = createTestDoctor("Jan", "Kowalski", 45, "98012112345", "Cardiology");
        Patient patient = createTestPatient("Anna", "Nowak", 30, "88010112345");
        Appointment appointment = createTestAppointment(doctor, patient, LocalDate.now());

        this.mockMvc.perform(delete("/api/v1/doctors/{doctorId}/appointments/cancel/{appointmentId}", doctor.getId(), appointment.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        assertFalse(appointmentRepository.existsById(appointment.getId()));
    }

    @Test
    void shouldGetDoctorPatients() throws Exception {
        Doctor doctor = createTestDoctor("Jan", "Kowalski", 45, "98012112345", "Cardiology");
        Patient patient1 = createTestPatient("Anna", "Nowak", 30, "88010112345");
        Patient patient2 = createTestPatient("Robert", "Lewandowski", 32, "85010112345");
        createTestAppointment(doctor, patient1, LocalDate.now());
        createTestAppointment(doctor, patient2, LocalDate.now().plusDays(1));

        this.mockMvc.perform(get("/api/v1/doctors/{doctorId}/patients", doctor.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Anna", "Robert")));
    }

    private Doctor createTestDoctor(String name, String surname, int age, String pesel, String specialization) {
        Doctor doctor = new Doctor();
        doctor.setName(name);
        doctor.setSurname(surname);
        doctor.setAge(age);
        doctor.setPesel(pesel);
        doctor.setSpecialization(specialization);
        return customerRepository.save(doctor);
    }

    private Patient createTestPatient(String name, String surname, int age, String pesel) {
        Patient patient = new Patient();
        patient.setName(name);
        patient.setSurname(surname);
        patient.setAge(age);
        patient.setPesel(pesel);
        return customerRepository.save(patient);
    }

    private Appointment createTestAppointment(Doctor doctor, Patient patient, LocalDate date) {
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setDate(date);
        return appointmentRepository.save(appointment);
    }
}