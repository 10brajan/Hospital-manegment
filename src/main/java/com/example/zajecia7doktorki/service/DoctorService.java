package com.example.zajecia7doktorki.service;

import com.example.zajecia7doktorki.command.DoctorCommand;
import com.example.zajecia7doktorki.domain.Appointment;
import com.example.zajecia7doktorki.domain.Doctor;
import com.example.zajecia7doktorki.domain.Patient;
import com.example.zajecia7doktorki.repository.DoctorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    public Doctor getDoctor(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(); //TODO DODAC WYJATEK
    }
    public List<Doctor> getAllDoctors() {return doctorRepository.findAll();}
    public Doctor createDoctor(Doctor doctor) {return doctorRepository.save(doctor);}

    public Doctor updateDoctor(Long id, DoctorCommand doctorCommand) {
        Doctor doctorToUpdate = doctorRepository.findById(id)
                .orElseThrow(); //TODO DODAC WYJATEK
        Optional.ofNullable(doctorCommand.getName()).ifPresent(doctorToUpdate::setName);
        Optional.ofNullable(doctorCommand.getSurname()).ifPresent(doctorToUpdate::setSurname);
        Optional.of(doctorCommand.getAge()).filter(age -> age > 0).ifPresent(doctorToUpdate::setAge);
        Optional.ofNullable(doctorCommand.getSpecialization()).ifPresent(doctorToUpdate::setSpecialization);
        return doctorRepository.save(doctorToUpdate);
    }

    public void deleteDoctor(Long id) {
        Doctor doctorToDelete = doctorRepository.findById(id)
                .orElseThrow(); //TODO DODAC WYJATEK
        doctorRepository.delete(doctorToDelete);
    }
    public List<Patient> getDoctorPatients(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono lekarza o podanym ID: " + doctorId));

        List<Appointment> doctorAppointments = doctor.getAppointments();
        List<Patient> patients = new ArrayList<>();

        for (Appointment appointment : doctorAppointments) {
            patients.add(appointment.getPatient());
        }

        return patients;
    }

}
