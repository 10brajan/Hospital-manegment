package com.example.zajecia7doktorki.service;

import com.example.zajecia7doktorki.command.PatientUpdateCommand;
import com.example.zajecia7doktorki.domain.Appointment;
import com.example.zajecia7doktorki.domain.Customer;
import com.example.zajecia7doktorki.domain.Patient;
import com.example.zajecia7doktorki.exception.LoginNotFoundException;
import com.example.zajecia7doktorki.exception.PatientNotFoundException;
import com.example.zajecia7doktorki.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {

    private static final String PATIENT = "PATIENT";
    private static final String PATIENT_WITH_THIS_LOGIN_DOES_NOT_EXIST = "Patient with this login does not exist";
    private static final String LOGIN_DOES_NOT_EXIST = "Login does not exist";


    private final CustomerRepository customerRepository;

    private final CustomerUserDetailsService userDetailsService;


    public Patient getPatient() {
        String username = Optional.ofNullable(userDetailsService.getUserDetails())
                .map(UserDetails::getUsername)
                .orElseThrow(() -> new LoginNotFoundException(LOGIN_DOES_NOT_EXIST));

        return customerRepository.findByLogin(username)
                .filter(Patient.class::isInstance)
                .map(Patient.class::cast)
                .orElseThrow(() -> new PatientNotFoundException(PATIENT_WITH_THIS_LOGIN_DOES_NOT_EXIST));

    }

    public List<Customer> getAllPatients() {
        return customerRepository.findAllByUserType(PATIENT);
    }

    public Patient updatePatient(PatientUpdateCommand patientUpdateCommand) {
//        Patient patientToUpdate = (Patient) customerRepository.findByLogin(userDetailsService.getUserDetails().getUsername())
//                .orElseThrow(() -> new PatientNotFoundException(PATIENT_WITH_THIS_LOGIN_DOES_NOT_EXIST));

        String username = Optional.ofNullable(userDetailsService.getUserDetails())
                .map(UserDetails::getUsername)
                .orElseThrow(() -> new LoginNotFoundException(LOGIN_DOES_NOT_EXIST));

        Patient patientToUpdate = customerRepository.findByLogin(username)
                .filter(Patient.class::isInstance)
                .map(Patient.class::cast)
                .orElseThrow(() -> new PatientNotFoundException(PATIENT_WITH_THIS_LOGIN_DOES_NOT_EXIST));

        Optional.ofNullable(patientUpdateCommand.getName()).ifPresent(patientToUpdate::setName);
        Optional.ofNullable(patientUpdateCommand.getSurname()).ifPresent(patientToUpdate::setSurname);
        Optional.of(patientUpdateCommand.getAge()).filter(age -> age > 0).ifPresent(patientToUpdate::setAge);
        return customerRepository.save(patientToUpdate);
    }

    public void deletePatient() {
        String username = Optional.ofNullable(userDetailsService.getUserDetails())
                .map(UserDetails::getUsername)
                .orElseThrow(() -> new LoginNotFoundException(LOGIN_DOES_NOT_EXIST));

        Patient patientToDelete = customerRepository.findByLogin(username)
                .filter(Patient.class::isInstance)
                .map(Patient.class::cast)
                .orElseThrow(() -> new PatientNotFoundException(PATIENT_WITH_THIS_LOGIN_DOES_NOT_EXIST));

        customerRepository.delete(patientToDelete);
    }

    public List<Appointment> getPatientAppointments() {
        String username = Optional.ofNullable(userDetailsService.getUserDetails())
                .map(UserDetails::getUsername)
                .orElseThrow(() -> new LoginNotFoundException(LOGIN_DOES_NOT_EXIST));

        Patient patient = customerRepository.findByLogin(username)
                .filter(Patient.class::isInstance)
                .map(Patient.class::cast)
                .orElseThrow(() -> new PatientNotFoundException(PATIENT_WITH_THIS_LOGIN_DOES_NOT_EXIST));

        return patient.getAppointments();
    }
}
