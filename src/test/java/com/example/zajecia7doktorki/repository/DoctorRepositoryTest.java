package com.example.zajecia7doktorki.repository;

import com.example.zajecia7doktorki.domain.Customer;
import com.example.zajecia7doktorki.domain.Doctor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
class DoctorRepositoryTest {
    @Autowired
    private CustomerRepository customerRepository;
    private Doctor doctor;
    @BeforeEach
    void setUp() {
        doctor = new Doctor();
        doctor.setName("Jan");
        doctor.setSurname("Kowalski");
        doctor.setSpecialization("Kardiolog");
        doctor.setAge(30);
        doctor.setPesel("12345678901");
    }


    @Test
    void findByPesel() {
        // given
        customerRepository.save(doctor);

        // when
        Optional<Customer> doctorByPesel = customerRepository.findByPesel(doctor.getPesel());

        // then
        assertThat(doctorByPesel).isNotEmpty();
        assertThat(doctorByPesel.get().getName()).isEqualTo(doctor.getName());
        assertThat(doctorByPesel.get().getSurname()).isEqualTo(doctor.getSurname());
//        assertThat(doctorByPesel.get().getSpecialization()).isEqualTo(doctor.getSpecialization());
    }

    @Test
    void shouldNotFindNonExistingPesel() {
        Optional<Customer> foundDoctor = customerRepository.findByPesel("nonExistingPesel");

        assertThat(foundDoctor).isEmpty();
    }

}