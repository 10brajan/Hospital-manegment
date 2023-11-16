package com.example.zajecia7doktorki.repository;

import com.example.zajecia7doktorki.domain.Customer;
import com.example.zajecia7doktorki.domain.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PatientRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    private Patient patient;

    @BeforeEach
    void setUp() {
       setUpPatient();
    }

    @Test
    void shouldFindByPesel() {
        // given
        customerRepository.save(patient);

        // when
        Optional<Customer> patientByPesel = customerRepository.findByPesel(patient.getPesel());

        // then
        assertThat(patientByPesel).isNotEmpty();
        assertThat(patientByPesel.get().getName()).isEqualTo(patient.getName());
        assertThat(patientByPesel.get().getSurname()).isEqualTo(patient.getSurname());
        assertThat(patientByPesel.get().getPesel()).isEqualTo(patient.getPesel());
        //sprawdzac id

    }


    @Test
    void shouldNotFindNonExistingPesel() {
        Optional<Patient> foundPatient = customerRepository.findByPesel("nonExistingPesel");

        assertThat(foundPatient).isEmpty();
    }

    private void setUpPatient() {
        patient = new Patient();
        patient.setName("Jan");
        patient.setSurname("Kowalski");
        patient.setAge(30);
        patient.setPesel("12345678901");
    }

}