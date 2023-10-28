package com.example.zajecia7doktorki.repository;

import com.example.zajecia7doktorki.domain.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
