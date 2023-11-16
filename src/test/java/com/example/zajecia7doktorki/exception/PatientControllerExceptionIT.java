package com.example.zajecia7doktorki.exception;

import com.example.zajecia7doktorki.command.AppointmentCommand;
import com.example.zajecia7doktorki.command.PatientCommand;
import com.example.zajecia7doktorki.domain.Doctor;
import com.example.zajecia7doktorki.domain.Patient;
import com.example.zajecia7doktorki.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("application-tests")
@AutoConfigureTestDatabase
public class PatientControllerExceptionIT {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .build();
    }

    @Test
    void shouldThrowValidationMessageWhenSavePatientWithInvalidName() throws Exception {
        PatientCommand patientCommand = new PatientCommand("a", "szymanski", 19, "11111111111", "amelia", "amelia");

        this.mockMvc.perform(post("/api/v1/patients")
                        .content(objectMapper.writeValueAsString(patientCommand))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> Assertions.assertEquals("Your name should contains between 3 to 20 letters",
                        (JsonPath.read(result.getResponse()
                                .getContentAsString(), "$.errors.name"))));
    }

    @Test
    void shouldThrowValidationMessageWhenSavePatientWithInvalidSurname() throws Exception {
        PatientCommand patientCommand = new PatientCommand("a", "szymanski", 19, "11111111111", "amelia", "amelia");


        this.mockMvc.perform(post("/api/v1/patients")
                        .content(objectMapper.writeValueAsString(patientCommand))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> Assertions.assertEquals("Your surname should contains between 3 to 20 letters",
                        (JsonPath.read(result.getResponse()
                                .getContentAsString(), "$.errors.surname"))));
    }


    @Test
    void shouldThrowValidationMessageWhenSavePatientWithInvalidAge() throws Exception {
        PatientCommand patientCommand = new PatientCommand("a", "szymanski", 19, "11111111111", "amelia", "amelia");

        this.mockMvc.perform(post("/api/v1/patients")
                        .content(objectMapper.writeValueAsString(patientCommand))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> Assertions.assertEquals("Age should not be zero",
                        (JsonPath.read(result.getResponse()
                                .getContentAsString(), "$.errors.age"))));
    }

    @Test
    void shouldThrowValidationMessageWhenSavePatientWithInvalidPesel() throws Exception {
        PatientCommand patientCommand = new PatientCommand("a", "szymanski", 19, "11111111111", "amelia", "amelia");

        this.mockMvc.perform(post("/api/v1/patients")
                        .content(objectMapper.writeValueAsString(patientCommand))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> Assertions.assertEquals("PESEL must be exactly 11 letters long",
                        (JsonPath.read(result.getResponse()
                                .getContentAsString(), "$.errors.pesel"))));
    }
    @Test
    void shouldThrowValidationMessageWhenMakeAppointmentWithInvalidDate() throws Exception {
        AppointmentCommand appointmentCommand = new AppointmentCommand(LocalDate.of(2021,12,12));
        Patient patient = createTestPatient("Brajan", "Szymanski", 25, "12345678901");
        Doctor doctor = createTestDoctor("Jan", "Kowalski", 45, "98012112345", "Cardiology");

        this.mockMvc.perform(post("/api/v1/patients/{patientId}/makeAppointment/{doctorId}", patient.getId(), doctor.getId())
                        .content(objectMapper.writeValueAsString(appointmentCommand))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> Assertions.assertEquals("APPOINTMENT DATE CAN NOT BE FROM THE PAST",
                        (JsonPath.read(result.getResponse()
                                .getContentAsString(), "$.errors.date"))));
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
