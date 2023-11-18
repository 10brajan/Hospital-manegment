package com.example.zajecia7doktorki.service;

import com.example.zajecia7doktorki.command.AppointmentCommand;
import com.example.zajecia7doktorki.domain.Appointment;
import com.example.zajecia7doktorki.domain.Doctor;
import com.example.zajecia7doktorki.domain.Patient;
import com.example.zajecia7doktorki.exception.*;
import com.example.zajecia7doktorki.model.Status;
import com.example.zajecia7doktorki.repository.AppointmentRepository;
import com.example.zajecia7doktorki.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private static final String DOCTOR_WITH_THIS_LOGIN_DOES_NOT_EXIST = "Doctor with this login does not exist";
    private static final String PATIENT_WITH_THIS_LOGIN_DOES_NOT_EXIST = "Patient with this login does not exist";

    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final PatientService patientService;
    private final CustomerUserDetailsService userDetailsService;


    public Appointment createAppointment(Appointment appointment, Long doctorId) {
        Patient patient = patientService.getPatient();
        if (!patient.isAccountNonLocked()) {
            throw new PermissionDeniedException("Your account is locked");
        }

        Doctor doctor = (Doctor) customerRepository.findByIdAndUserType(doctorId, "DOCTOR")
                .orElseThrow(() -> new DoctorNotFoundException("Doctor with this id does not exist"));

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        if (appointmentRepository.existsByPatientAndDoctorAndDate(patient, doctor, appointment.getDate())) {
            throw new AppointmentConflictException("Patient is already signed to this appointment");
        }

        return appointmentRepository.save(appointment);
    }

    public void cancelAppointmentByDoctor(Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment with id " + appointmentId + " not found"));

        String username = Optional.ofNullable(userDetailsService.getUserDetails())
                .map(UserDetails::getUsername)
                .orElseThrow(() -> new LoginNotFoundException("Login does not exist"));

        Doctor doctor = customerRepository.findByLogin(username)
                .filter(Doctor.class::isInstance)
                .map(Doctor.class::cast)
                .orElseThrow(() -> new DoctorNotFoundException(DOCTOR_WITH_THIS_LOGIN_DOES_NOT_EXIST));

//        if (!appointment.getDoctor().getId().equals(doctor.getId())) {
        if (!Objects.equals(appointment.getDoctor().getId(), doctor.getId())) {
            throw new PermissionDeniedException("Doctor can only cancel their own appointments");
        }

        appointment.setStatus(Status.CANCELLED);
        appointmentRepository.save(appointment);
    }

    public void cancelAppointmentByPatient(Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment with id " + appointmentId + " not found"));

        String username = Optional.ofNullable(userDetailsService.getUserDetails())
                .map(UserDetails::getUsername)
                .orElseThrow(() -> new LoginNotFoundException("Login does not exist"));

        Patient patient = customerRepository.findByLogin(username)
                .filter(Patient.class::isInstance)
                .map(Patient.class::cast)
                .orElseThrow(() -> new PatientNotFoundException(PATIENT_WITH_THIS_LOGIN_DOES_NOT_EXIST));

        if (!Objects.equals(appointment.getPatient().getId(), patient.getId())) {
            throw new PermissionDeniedException("Patient can only cancel their own appointments");
        }

        appointment.setStatus(Status.CANCELLED);
        appointmentRepository.save(appointment);
    }

    public void appointmentSuccess(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment with id " + appointmentId + " not found"));

        String username = Optional.ofNullable(userDetailsService.getUserDetails())
                .map(UserDetails::getUsername)
                .orElseThrow(() -> new LoginNotFoundException("Login does not exist"));

        Doctor doctor = customerRepository.findByLogin(username)
                .filter(Doctor.class::isInstance)
                .map(Doctor.class::cast)
                .orElseThrow(() -> new DoctorNotFoundException(DOCTOR_WITH_THIS_LOGIN_DOES_NOT_EXIST));

        if (!Objects.equals(appointment.getDoctor().getId(), doctor.getId())) {
            throw new PermissionDeniedException("Doctor can only cancel their own appointments");
        }
        appointment.setStatus(Status.SUCCESSFUL);
        appointmentRepository.save(appointment);
    }

    public Appointment updateAppointment(Long id, AppointmentCommand appointmentCommand) {
        Appointment appointmentToUpdate = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment with this id does not exist"));

        Optional.ofNullable(appointmentCommand.getDate())
                .ifPresent(appointmentToUpdate::setDate);

        return appointmentRepository.save(appointmentToUpdate);
    }

    public void deleteAppointment(Long id) {
        Appointment appointmentToDelete = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment with this id does not exist"));
        appointmentRepository.delete(appointmentToDelete);
    }

}
