package com.example.zajecia7doktorki.controller;

import com.example.zajecia7doktorki.command.AdminCommand;
import com.example.zajecia7doktorki.command.DoctorCommand;
import com.example.zajecia7doktorki.command.PatientCommand;
import com.example.zajecia7doktorki.dto.AdminDTO;
import com.example.zajecia7doktorki.dto.DoctorDTO;
import com.example.zajecia7doktorki.dto.PatientDTO;
import com.example.zajecia7doktorki.mapping.AdminMapper;
import com.example.zajecia7doktorki.mapping.DoctorMapper;
import com.example.zajecia7doktorki.mapping.PatientMapper;
import com.example.zajecia7doktorki.service.RegisterService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Tag(name = "Register controller", description = "Thanks to hospital api we can create a new admin")
@RequestMapping("/api/v1/register")
public class RegisterController {
    private final RegisterService registerService;
    private final DoctorMapper doctorMapper;
    private final PatientMapper patientMapper;
    private final AdminMapper adminMapper;


    @PostMapping("/patient")
    public ResponseEntity<PatientDTO> savePatient(@Valid @RequestBody PatientCommand patientCommand) {
        return new ResponseEntity<>(patientMapper.patientEntityToDTO(registerService
                .createPatient(patientMapper.patientCommandToPatientEntity(patientCommand))), HttpStatus.CREATED);
    }

    @PostMapping("/doctor")
    public ResponseEntity<DoctorDTO> saveDoctor(@RequestBody @Valid DoctorCommand doctorCommand) {
        return new ResponseEntity<>(doctorMapper.doctorEntityToDTO(registerService
                .createDoctor(doctorMapper.doctorCommandToDoctorEntity(doctorCommand))), HttpStatus.CREATED);
    }

    @PostMapping("/admin")
    public ResponseEntity<AdminDTO> saveAdmin(@RequestBody @Valid AdminCommand adminCommand) {
        return new ResponseEntity<>(adminMapper.adminEntityToDTO(registerService
                .createAdmin(adminMapper.adminCommandToAdminEntity(adminCommand))), HttpStatus.CREATED);
    }
}
