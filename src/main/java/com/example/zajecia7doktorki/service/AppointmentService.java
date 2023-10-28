package com.example.zajecia7doktorki.service;

import com.example.zajecia7doktorki.command.AppointmentCommand;
import com.example.zajecia7doktorki.command.PatientCommand;
import com.example.zajecia7doktorki.domain.Appointment;
import com.example.zajecia7doktorki.domain.Doctor;
import com.example.zajecia7doktorki.domain.Patient;
import com.example.zajecia7doktorki.exception.AppointmentConflictException;
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
    public List<Appointment> getAllAppointments() {return appointmentRepository.findAll();}
    public Appointment createAppointment(Appointment appointment) {return appointmentRepository.save(appointment);}
//    public Appointment createAppointment(AppointmentCommand appointmentCommand) {
//        Patient patient = patientService.getPatient(appointmentCommand.getPatientId());
//        Doctor doctor = doctorService.getDoctor(appointmentCommand.getDoctorId());
//
//        Appointment appointment = new Appointment();
//        appointment.setDate(appointmentCommand.getDate());
//        appointment.setPatient(patient);
//        appointment.setDoctor(doctor);
//
//        return appointmentRepository.save(appointment);
//    }

    public Appointment createAppointment(AppointmentCommand appointmentCommand) {
        Patient patient = patientService.getPatient(appointmentCommand.getPatientId());
        Doctor doctor = doctorService.getDoctor(appointmentCommand.getDoctorId());

        // Sprawdź, czy pacjent jest już zapisany na tę wizytę
        if (appointmentRepository.existsByPatientAndDoctorAndDate(patient, doctor, appointmentCommand.getDate())) {
            throw new AppointmentConflictException("Pacjent jest już zapisany na tę wizytę.");
        }

        Appointment appointment = new Appointment();
        appointment.setDate(appointmentCommand.getDate());
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        return appointmentRepository.save(appointment);
    }

    public void cancelAppointment(Long id, Long doctorId) {
        Appointment appointment = getAppointment(id);
        Doctor doctor = appointment.getDoctor();

        // Sprawdź, czy lekarz odwołuje własną wizytę
        if (!doctor.getId().equals(doctorId)) {
            throw new PermissionDeniedException("Nie masz uprawnień do odwoływania tej wizyty.");
        }

        appointmentRepository.delete(appointment);
    }

    public Appointment updateAppointment(Long id, AppointmentCommand appointmentCommand) {
        Appointment appointmentToUpdate = appointmentRepository.findById(id)
                .orElseThrow(); //TODO DODAC WYJATEK

        Optional.ofNullable(appointmentCommand.getDate()).ifPresent(appointmentToUpdate::setDate);

        return appointmentRepository.save(appointmentToUpdate);
    }

    public void deleteAppointment(Long id) {
        Appointment appointmentToDelete = appointmentRepository.findById(id)
                .orElseThrow(); //TODO DODAC WYJATEK
        appointmentRepository.delete(appointmentToDelete);
    }

}
