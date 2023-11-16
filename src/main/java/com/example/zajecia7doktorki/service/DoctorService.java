package com.example.zajecia7doktorki.service;

import com.example.zajecia7doktorki.command.DoctorCommand;
import com.example.zajecia7doktorki.domain.Appointment;
import com.example.zajecia7doktorki.domain.Customer;
import com.example.zajecia7doktorki.domain.Doctor;
import com.example.zajecia7doktorki.domain.Patient;
import com.example.zajecia7doktorki.exception.DoctorNotFoundException;
import com.example.zajecia7doktorki.exception.LoginNotFoundException;
import com.example.zajecia7doktorki.exception.PatientNotFoundException;
import com.example.zajecia7doktorki.exception.PermissionDeniedException;
import com.example.zajecia7doktorki.model.HealthCondition;
import com.example.zajecia7doktorki.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private static final String DOCTOR = "DOCTOR";
    private static final String DOCTOR_WITH_THIS_LOGIN_DOES_NOT_EXIST = "Doctor with this login does not exist";

    private final CustomerRepository customerRepository;

    private final CustomerUserDetailsService userDetailsService;

    public Doctor getDoctor() {
        String username = Optional.ofNullable(userDetailsService.getUserDetails())
                .map(UserDetails::getUsername)
                .orElseThrow(() -> new LoginNotFoundException("Login does not exist"));

       return customerRepository.findByLogin(username)
                .filter(Doctor.class::isInstance)
                .map(Doctor.class::cast)
                .orElseThrow(() -> new DoctorNotFoundException(DOCTOR_WITH_THIS_LOGIN_DOES_NOT_EXIST));
    }

    public List<Customer> getAllDoctors() {
        return customerRepository.findAllByUserType(DOCTOR);
    }

    public Doctor updateDoctor(DoctorCommand doctorCommand) {
//        Doctor doctorToUpdate = (Doctor) customerRepository.findByIdAndUserType(id, DOCTOR)
//                .orElseThrow(() -> new DoctorNotFoundException("Doctor with this id does not exist"));

//        Doctor doctorToUpdate = (Doctor) customerRepository.findByLogin(userDetailsService.getUserDetails().getUsername())
//                .orElseThrow(() -> new DoctorNotFoundException(MESSAGE));

        String username = Optional.ofNullable(userDetailsService.getUserDetails())
                .map(UserDetails::getUsername)
                .orElseThrow(() -> new LoginNotFoundException("Login does not exist"));

        Doctor doctorToUpdate = customerRepository.findByLogin(username)
                .filter(Doctor.class::isInstance)
                .map(Doctor.class::cast)
                .orElseThrow(() -> new DoctorNotFoundException(DOCTOR_WITH_THIS_LOGIN_DOES_NOT_EXIST));


        Optional.ofNullable(doctorCommand.getName())
                .ifPresent(doctorToUpdate::setName);
        Optional.ofNullable(doctorCommand.getSurname())
                .ifPresent(doctorToUpdate::setSurname);
        Optional.of(doctorCommand.getAge()).filter(age -> age > 0)
                .ifPresent(doctorToUpdate::setAge);
        Optional.ofNullable(doctorCommand.getSpecialization())
                .ifPresent(doctorToUpdate::setSpecialization);
        Optional.ofNullable(doctorCommand.getLogin())
                .ifPresent(doctorToUpdate::setLogin);
        Optional.ofNullable(doctorCommand.getPassword())
                .ifPresent(doctorToUpdate::setPassword);
        return customerRepository.save(doctorToUpdate);
    }

    public void deleteDoctor() {
        String username = Optional.ofNullable(userDetailsService.getUserDetails())
                .map(UserDetails::getUsername)
                .orElseThrow(() -> new LoginNotFoundException("Login does not exist"));

        Doctor doctorToDelete = customerRepository.findByLogin(username)
                .filter(Doctor.class::isInstance)
                .map(Doctor.class::cast)
                .orElseThrow(() -> new DoctorNotFoundException(DOCTOR_WITH_THIS_LOGIN_DOES_NOT_EXIST));
        customerRepository.delete(doctorToDelete);
    }

    public Set<Patient> getDoctorPatients() {
        String username = Optional.ofNullable(userDetailsService.getUserDetails())
                .map(UserDetails::getUsername)
                .orElseThrow(() -> new LoginNotFoundException("Login does not exist"));

        Doctor doctor = customerRepository.findByLogin(username)
                .filter(Doctor.class::isInstance)
                .map(Doctor.class::cast)
                .orElseThrow(() -> new DoctorNotFoundException(DOCTOR_WITH_THIS_LOGIN_DOES_NOT_EXIST));

        List<Appointment> doctorAppointments = doctor.getAppointments();
        Set<Patient> patients = new HashSet<>();

        doctorAppointments.forEach(appointment -> patients.add(appointment.getPatient()));
        return patients;
    }

    public void changeHealthCondition(Long patientId, String healthCondition) {
        Patient patient = (Patient) customerRepository.findByIdAndUserType(patientId, "PATIENT")
                .orElseThrow(() -> new PatientNotFoundException("Patient with this id does not exist"));

        if (getDoctorPatients().contains(patient)) {
            patient.setHealthCondition(HealthCondition.valueOf(healthCondition));
        } else {
            throw new PermissionDeniedException("Doctor can only change health condition of his patients");
        }
        customerRepository.save(patient);
    }

    public List<Appointment> getDoctorAppointments() {
        String username = Optional.ofNullable(userDetailsService.getUserDetails())
                .map(UserDetails::getUsername)
                .orElseThrow(() -> new LoginNotFoundException("Login does not exist"));

        Doctor doctor = customerRepository.findByLogin(username)
                .filter(Doctor.class::isInstance)
                .map(Doctor.class::cast)
                .orElseThrow(() -> new DoctorNotFoundException(DOCTOR_WITH_THIS_LOGIN_DOES_NOT_EXIST));

        return doctor.getAppointments();
    }
}
