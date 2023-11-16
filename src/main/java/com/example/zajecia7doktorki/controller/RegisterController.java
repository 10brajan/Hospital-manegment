package com.example.zajecia7doktorki.controller;

import com.example.zajecia7doktorki.command.AdminCommand;
import com.example.zajecia7doktorki.command.DoctorCommand;
import com.example.zajecia7doktorki.command.PatientCommand;
import com.example.zajecia7doktorki.domain.Admin;
import com.example.zajecia7doktorki.domain.Doctor;
import com.example.zajecia7doktorki.domain.Patient;
import com.example.zajecia7doktorki.dto.AdminDTO;
import com.example.zajecia7doktorki.dto.DoctorDTO;
import com.example.zajecia7doktorki.dto.PatientDTO;
import com.example.zajecia7doktorki.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/register")
public class RegisterController {
    private final RegisterService registerService;
    private final ModelMapper modelMapper;

    @PostMapping("/patient")
    public ResponseEntity<PatientDTO> savePatient(@Valid @RequestBody PatientCommand patientCommand) {
        return new ResponseEntity<>(modelMapper
                .map(registerService.createPatient(modelMapper
                        .map(patientCommand, Patient.class)), PatientDTO.class), HttpStatus.CREATED);
    }

    @PostMapping("/doctor")
    public ResponseEntity<DoctorDTO> saveDoctor(@RequestBody @Valid DoctorCommand doctorCommand) {
        return new ResponseEntity<>(modelMapper
                .map(registerService.createDoctor(modelMapper
                        .map(doctorCommand, Doctor.class)), DoctorDTO.class), HttpStatus.CREATED);
    }

    @PostMapping("/admin")
    public ResponseEntity<AdminDTO> saveAdmin(@RequestBody @Valid AdminCommand adminCommand) {
        return new ResponseEntity<>(modelMapper
                .map(registerService.createAdmin(modelMapper
                        .map(adminCommand, Admin.class)), AdminDTO.class), HttpStatus.CREATED);
    }
}
