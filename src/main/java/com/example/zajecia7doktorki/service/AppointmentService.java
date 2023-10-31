package com.example.zajecia7doktorki.service;

import com.example.zajecia7doktorki.command.AppointmentCommand;
import com.example.zajecia7doktorki.domain.Appointment;
import com.example.zajecia7doktorki.domain.Doctor;
import com.example.zajecia7doktorki.domain.Patient;
import com.example.zajecia7doktorki.exception.AppointmentConflictException;
import com.example.zajecia7doktorki.exception.AppointmentNotFoundException;
import com.example.zajecia7doktorki.exception.PermissionDeniedException;
import com.example.zajecia7doktorki.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientService patientService;
    private final DoctorService doctorService;

    public Appointment getAppointment(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow();
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }


    public Appointment createAppointment(Appointment appointment, Long patientId, Long doctorId) {
        Patient patient = patientService.getPatient(patientId);
        Doctor doctor = doctorService.getDoctor(doctorId);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        if (appointmentRepository.existsByPatientAndDoctorAndDate(patient, doctor, appointment.getDate())) {
            throw new AppointmentConflictException("Patient is already signed to this appointment");
        }
        return appointmentRepository.save(appointment);
    }

    public void cancelAppointment(Long appointmentId, Long doctorId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment with id " + appointmentId + " not found"));
        if (!appointment.getDoctor().getId().equals(doctorId)) {
            throw new PermissionDeniedException("Doctor can only cancel their own appointments");
        }

        appointmentRepository.delete(appointment);
    }

    public Appointment updateAppointment(Long id, AppointmentCommand appointmentCommand) {
        Appointment appointmentToUpdate = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment with this id does not exist"));

        Optional.ofNullable(appointmentCommand.getDate()).ifPresent(appointmentToUpdate::setDate);

        return appointmentRepository.save(appointmentToUpdate);
    }

    public void deleteAppointment(Long id) {
        Appointment appointmentToDelete = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment with this id does not exist"));
        appointmentRepository.delete(appointmentToDelete);
    }

}
