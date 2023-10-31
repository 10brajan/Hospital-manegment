package com.example.zajecia7doktorki.controller;

import com.example.zajecia7doktorki.command.DoctorCommand;
import com.example.zajecia7doktorki.domain.Doctor;
import com.example.zajecia7doktorki.domain.Patient;
import com.example.zajecia7doktorki.dto.DoctorDTO;
import com.example.zajecia7doktorki.dto.PatientDTO;
import com.example.zajecia7doktorki.service.AppointmentService;
import com.example.zajecia7doktorki.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/hospital/doctors")
public class DoctorController {
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final ModelMapper modelMapper;

    @PostMapping("/add")
    public ResponseEntity<DoctorDTO> saveDoctor(@RequestBody @Valid DoctorCommand doctorCommand) {
        return new ResponseEntity<>(modelMapper
                .map(doctorService.createDoctor(modelMapper
                        .map(doctorCommand, Doctor.class)), DoctorDTO.class), HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<DoctorDTO> findById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(modelMapper.map(doctorService.getDoctor(id), DoctorDTO.class), HttpStatus.OK);
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<DoctorDTO>> getDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return new ResponseEntity<>(doctors.stream()
                .map(doctor -> modelMapper
                        .map(doctor, DoctorDTO.class)).toList(), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DoctorDTO> updateDoctor(@PathVariable("id") Long id, @RequestBody DoctorCommand doctorCommand) {
        Doctor updatedDoctor = doctorService.updateDoctor(id, doctorCommand);
        return new ResponseEntity<>(modelMapper.map(updatedDoctor, DoctorDTO.class), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public HttpStatus deleteDoctor(@PathVariable("id") Long id) {
        doctorService.deleteDoctor(id);
        return HttpStatus.OK;
    }
    @DeleteMapping("/{doctorId}/appointments/cancel/{appointmentId}")
    public HttpStatus cancelAppointment(@PathVariable("appointmentId") Long appointmentId, @PathVariable("doctorId") Long doctorId) {
        appointmentService.cancelAppointment(appointmentId, doctorId);
        return HttpStatus.OK;
    }

    @GetMapping("/{doctorId}/patients")
    public ResponseEntity<List<PatientDTO>> getDoctorPatients(@PathVariable("doctorId") Long doctorId) {
        List<Patient> patients = doctorService.getDoctorPatients(doctorId);
        return new ResponseEntity<>(patients.stream()
                .map(patient -> modelMapper.map(patient, PatientDTO.class))
                .toList(), HttpStatus.OK);
    }
}
