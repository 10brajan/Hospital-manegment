package com.example.zajecia7doktorki.controller;

import com.example.zajecia7doktorki.command.AppointmentCommand;
import com.example.zajecia7doktorki.command.PatientCommand;
import com.example.zajecia7doktorki.domain.Doctor;
import com.example.zajecia7doktorki.domain.Patient;
import com.example.zajecia7doktorki.repository.AppointmentRepository;
import com.example.zajecia7doktorki.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("application-test")
//@AutoConfigureMockMvc
@AutoConfigureTestDatabase
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PatientControllerIT {
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
    void shouldCreatePatient() throws Exception {
        PatientCommand patientCommand = new PatientCommand("Rychu", "Zbychu", 15, "12345678910", "Rychu", "Rychu");

        this.mockMvc.perform(post("/api/v1/patients")
                        .content(objectMapper.writeValueAsString(patientCommand))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Rychu"));
    }

    @Test
    void shouldFindPatientById() throws Exception {
        Patient patient = createTestPatient("Rychu", "Peja", 40, "22222222222");

        this.mockMvc.perform(get("/api/v1/patients/{id}", patient.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Rychu"))
                .andExpect(jsonPath("$.surname").value("Peja"));
    }

    @Test
    void shouldGetAllPatients() throws Exception {
        createTestPatient("Anna", "Nowak", 39, "78050512345");
        createTestPatient("Jan", "Kowalski", 45, "98012112345");

        this.mockMvc.perform(get("/api/v1/patients")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Anna"))
                .andExpect(jsonPath("$[1].name").value("Jan"));
    }

    @Test
    void shouldUpdatePatient() throws Exception {
        Patient patient = createTestPatient("Anna", "Nowak", 39, "78050512345");
        PatientCommand patientCommand = new PatientCommand("Rychu", "Zbychu", 15, "12345678910", "Rychu", "Rychu");


        this.mockMvc.perform(put("/api/v1/patients/{patientId}", patient.getId())
                        .content(objectMapper.writeValueAsString(patientCommand))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Rychu"))
                .andExpect(jsonPath("$.surname").value("Zbychu"));
    }

    @Test
    void shouldDeletePatient() throws Exception {
        Patient patient = createTestPatient("Brajan", "Szymanski", 25, "12345678901");
        this.mockMvc.perform(delete("/api/v1/patients/{patientID}", patient.getId()))
                .andExpect(status().isOk());
        //dodac dodadkowe assercje
    }

    @Test
    void shouldCreateAppointment() throws Exception {
        Doctor doctor = createTestDoctor("Jan", "Kowalski", 45, "98012112345", "Cardiology");
        Patient patient = createTestPatient("Anna", "Nowak", 30, "88010112345");
        AppointmentCommand appointmentCommand = new AppointmentCommand(LocalDate.of(2023, 12, 12));


        this.mockMvc.perform(put("/api/v1/patients/{patientId}/makeAppointment/{doctorId}", patient.getId(), doctor.getId())
                        .content(objectMapper.writeValueAsString(appointmentCommand))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
//        assertThat(patient.getAppointments()).isNotEmpty();
//        Doctor updatedDoctor = doctorRepository.findById(doctor.getId()).orElseThrow();
//        Patient updatedPatient = patientRepository.findById(patient.getId()).orElseThrow();
//
//        assertTrue(updatedDoctor.getAppointments().stream().anyMatch(appointment ->
//                appointment.getDate().equals(appointmentCommand.getDate())));
//        assertTrue(updatedPatient.getAppointments().stream().anyMatch(appointment ->
//                appointment.getDate().equals(appointmentCommand.getDate())));

    }

    @Test
    void shouldGetPatientAppointments() throws Exception {
        Patient patient = createTestPatient("Brajan", "Szymanski", 25, "12345678901");

        mockMvc.perform(get("/api/v1/patients/{patientId}/appointments/", patient.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
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
}
