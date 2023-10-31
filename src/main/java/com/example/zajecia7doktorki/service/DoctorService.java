package com.example.zajecia7doktorki.service;

import com.example.zajecia7doktorki.command.DoctorCommand;
import com.example.zajecia7doktorki.domain.Appointment;
import com.example.zajecia7doktorki.domain.Doctor;
import com.example.zajecia7doktorki.domain.Patient;
import com.example.zajecia7doktorki.exception.DoctorAlreadyExistsException;
import com.example.zajecia7doktorki.exception.DoctorNotFoundException;
import com.example.zajecia7doktorki.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;

    public Doctor getDoctor(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor with this id does not exist"));
    }

    public Doctor createDoctor(Doctor doctor) {
        if (doctorRepository.findByPesel(doctor.getPesel()).isPresent()) {
            throw new DoctorAlreadyExistsException("Doctor with this pesel already exists");
        }
        //tenansy
        return doctorRepository.save(doctor);
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor updateDoctor(Long id, DoctorCommand doctorCommand) {
        Doctor doctorToUpdate = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor with this id does not exist"));
        Optional.ofNullable(doctorCommand.getName())
                .ifPresent(doctorToUpdate::setName);
        Optional.ofNullable(doctorCommand.getSurname())
                .ifPresent(doctorToUpdate::setSurname);
        Optional.of(doctorCommand.getAge()).filter(age -> age > 0)
                .ifPresent(doctorToUpdate::setAge);
        Optional.ofNullable(doctorCommand.getSpecialization())
                .ifPresent(doctorToUpdate::setSpecialization);
        return doctorRepository.save(doctorToUpdate);
    }

    public void deleteDoctor(Long id) {
        Doctor doctorToDelete = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor with this id does not exist"));
        doctorRepository.delete(doctorToDelete);
    }

    public Set<Patient> getDoctorPatients(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor with this id does not exist"));

        List<Appointment> doctorAppointments = doctor.getAppointments();
        Set<Patient> patients = new HashSet<>();

        doctorAppointments.forEach(appointment -> patients.add(appointment.getPatient()));
//        .forEach
        return patients;
    }

}
