package com.example.zajecia7doktorki.exception;

import com.example.zajecia7doktorki.command.DoctorCommand;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("application-tests")
@AutoConfigureTestDatabase
class DoctorControllerExceptionIT {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .build();
    }

    @Test
    void shouldThrowValidationMessageWhenSaveDoctorWithInvalidName() throws Exception {
        DoctorCommand doctorCommand = new DoctorCommand("   ", "szymanski", "Kardiolog", 29, "1111111111", "brajan", "brajan");

        this.mockMvc.perform(post("/api/v1/doctors")
                        .content(objectMapper.writeValueAsString(doctorCommand))
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
    void shouldThrowValidationMessageWhenSaveDoctorWithInvalidSurname() throws Exception {
        DoctorCommand doctorCommand = new DoctorCommand("   ", "szymanski", "Kardiolog", 29, "1111111111", "brajan", "brajan");

        this.mockMvc.perform(post("/api/v1/doctors")
                        .content(objectMapper.writeValueAsString(doctorCommand))
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
    void shouldThrowValidationMessageWhenSaveDoctorWithInvalidSpecialization() throws Exception {
        DoctorCommand doctorCommand = new DoctorCommand("   ", "szymanski", "Kardiolog", 29, "1111111111", "brajan", "brajan");

        this.mockMvc.perform(post("/api/v1/doctors")
                        .content(objectMapper.writeValueAsString(doctorCommand))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> Assertions.assertEquals("Specialization should not be blank",
                        (JsonPath.read(result.getResponse()
                                .getContentAsString(), "$.errors.specialization"))));
    }


    @Test
    void shouldThrowValidationMessageWhenSaveDoctorWithInvalidAge() throws Exception {
        DoctorCommand doctorCommand = new DoctorCommand("   ", "szymanski", "Kardiolog", 29, "1111111111", "brajan", "brajan");

        this.mockMvc.perform(post("/api/v1/doctors")
                        .content(objectMapper.writeValueAsString(doctorCommand))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> Assertions.assertEquals("To be a doctor your age must be higher than 25",
                        (JsonPath.read(result.getResponse()
                                .getContentAsString(), "$.errors.age"))));
    }

    @Test
    void shouldThrowValidationMessageWhenSaveDoctorWithInvalidPesel() throws Exception {
        DoctorCommand doctorCommand = new DoctorCommand("   ", "szymanski", "Kardiolog", 29, "1111111111", "brajan", "brajan");

        this.mockMvc.perform(post("/api/v1/doctors")
                        .content(objectMapper.writeValueAsString(doctorCommand))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> Assertions.assertEquals("PESEL must be exactly 11 letters long",
                        (JsonPath.read(result.getResponse()
                                .getContentAsString(), "$.errors.pesel"))));
    }
}
