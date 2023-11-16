package com.example.zajecia7doktorki.repository;

import com.example.zajecia7doktorki.domain.Customer;
import com.example.zajecia7doktorki.domain.Doctor;
import com.example.zajecia7doktorki.domain.Patient;
import liquibase.pro.packaged.T;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByIdAndUserType(Long id, String userType);

    List<Customer> findAllByUserType(String type);

    Optional<Customer> findByLogin(String login);

    Optional<Customer> findByPesel(String pesel);

}
