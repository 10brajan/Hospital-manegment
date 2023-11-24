package com.example.zajecia7doktorki.controller;

import com.example.zajecia7doktorki.command.AppointmentCommand;
import com.example.zajecia7doktorki.command.PatientUpdateCommand;
import com.example.zajecia7doktorki.dto.AppointmentDTO;
import com.example.zajecia7doktorki.dto.PatientDTO;
import com.example.zajecia7doktorki.mapping.AppointmentMapper;
import com.example.zajecia7doktorki.mapping.PatientMapper;
import com.example.zajecia7doktorki.service.AppointmentService;
import com.example.zajecia7doktorki.service.PatientService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Tag(name = "Patient controller", description = "Thanks to hospital api we can create a new patient")
@RequestMapping("/api/v1/patients")
public class PatientController {

    private final PatientService patientService;

    private final AppointmentService appointmentService;
    private final PatientMapper patientMapper;
    private final AppointmentMapper appointmentMapper;

    @GetMapping("/get")
    public ResponseEntity<PatientDTO> getPatient() {
        return new ResponseEntity<>(patientMapper.patientEntityToDTO(patientService.getPatient()), HttpStatus.OK);
    }

    @GetMapping("/getAll")
    //    dodac paginacje tutaj
    public ResponseEntity<Page<PatientDTO>> getPatients(@PageableDefault Pageable pageable) {
        return new ResponseEntity<>(patientService.getAllPatients(pageable)
                .map(patientMapper::patientEntityToDTO), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<PatientDTO> updatePatient(@RequestBody PatientUpdateCommand patientUpdateCommand) {
        return new ResponseEntity<>(patientMapper.patientEntityToDTO
                (patientService.updatePatient(patientUpdateCommand)), HttpStatus.OK);
    }

    @DeleteMapping
    public HttpStatus deletePatient() {
        patientService.deletePatient();
        return HttpStatus.OK;
    }

    @PutMapping("/makeAppointment/{doctorId}")
    public ResponseEntity<AppointmentDTO> createAppointment(@PathVariable("doctorId") Long doctorId,
                                                            @RequestBody @Valid AppointmentCommand appointmentCommand) {
        return new ResponseEntity<>(appointmentMapper.appointmentEntityToDTO((appointmentService
                .createAppointment(appointmentMapper.appointmentCommandToAppointmentEntity(appointmentCommand), doctorId))), HttpStatus.CREATED);
    }

    @PutMapping("/appointments/cancel/{appointmentId}")
    public HttpStatus cancelAppointment(@PathVariable("appointmentId") Long appointmentId) {
        appointmentService.cancelAppointmentByPatient(appointmentId);
        return HttpStatus.OK;
    }

    @GetMapping("/appointments")
    //    dodac paginacje tutaj
    public ResponseEntity<Page<AppointmentDTO>> getPatientAppointments(@PageableDefault Pageable pageable) {
        return new ResponseEntity<>(patientService.getPatientAppointments(pageable)
                .map(appointmentMapper::appointmentEntityToDTO), HttpStatus.OK);
    }
}
