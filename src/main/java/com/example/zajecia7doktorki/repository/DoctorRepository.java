package com.example.zajecia7doktorki.repository;

import com.example.zajecia7doktorki.domain.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByPesel(String pesel);
}
