package com.example.zajecia7doktorki.service;

import com.example.zajecia7doktorki.command.DoctorCommand;
import com.example.zajecia7doktorki.command.PatientCommand;
import com.example.zajecia7doktorki.domain.Appointment;
import com.example.zajecia7doktorki.domain.Doctor;
import com.example.zajecia7doktorki.domain.Patient;
import com.example.zajecia7doktorki.exception.DuplicateEntityException;
import com.example.zajecia7doktorki.exception.PatientNotFoundException;
import com.example.zajecia7doktorki.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;

    public Patient getPatient(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient with this id does not exist"));
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient createPatient(Patient patient) {
        if (patientRepository.findByPesel(patient.getPesel()).isPresent()) {
            throw new DuplicateEntityException("Pacjent z takim PESELem już istnieje!");
        }
        return patientRepository.save(patient);
    }

    public Patient updatePatient(Long id, PatientCommand patientCommand) {
        Patient patientToUpdate = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient with this id does not exist"));

        Optional.ofNullable(patientCommand.getName()).ifPresent(patientToUpdate::setName);
        Optional.ofNullable(patientCommand.getSurname()).ifPresent(patientToUpdate::setSurname);
        Optional.of(patientCommand.getAge()).filter(age -> age > 0).ifPresent(patientToUpdate::setAge);
        Optional.ofNullable(patientCommand.getPesel()).ifPresent(patientToUpdate::setPesel);
        return patientRepository.save(patientToUpdate);
    }

    public void deletePatient(Long id) {
        Patient patientToDelete = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient with this id does not exist"));
        patientRepository.delete(patientToDelete);
    }

    public List<Appointment> getPatientAppointments(Long patientId) {
        Patient patient = getPatient(patientId);
        return patient.getAppointments();
    }

}
