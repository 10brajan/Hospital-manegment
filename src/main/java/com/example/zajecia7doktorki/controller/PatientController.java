package com.example.zajecia7doktorki.controller;

import com.example.zajecia7doktorki.command.AppointmentCommand;
import com.example.zajecia7doktorki.command.PatientUpdateCommand;
import com.example.zajecia7doktorki.domain.Appointment;
import com.example.zajecia7doktorki.domain.Patient;
import com.example.zajecia7doktorki.dto.AppointmentDTO;
import com.example.zajecia7doktorki.dto.PatientDTO;
import com.example.zajecia7doktorki.mapping.AppointmentMapper;
import com.example.zajecia7doktorki.mapping.PatientMapper;
import com.example.zajecia7doktorki.service.AppointmentService;
import com.example.zajecia7doktorki.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/patients")
public class PatientController {

    private final PatientService patientService;

    private final AppointmentService appointmentService;

    @GetMapping("/get")
    public ResponseEntity<PatientDTO> findById() {
        return new ResponseEntity<>(PatientMapper.INSTANCE.patientEnityToDTO(patientService.getPatient()), HttpStatus.OK);
    }

    @GetMapping
    //    dodac paginacje tutaj
    public ResponseEntity<List<PatientDTO>> getPatients() {
        return new ResponseEntity<>(patientService.getAllPatients().stream()
                .map(patient -> PatientMapper.INSTANCE.patientEnityToDTO((Patient) patient)).toList(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<PatientDTO> updatePatient(@RequestBody PatientUpdateCommand patientUpdateCommand) {
        return new ResponseEntity<>(PatientMapper.INSTANCE.patientEnityToDTO
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
        return new ResponseEntity<>(AppointmentMapper.INSTANCE.appointmentEnityToDTO((appointmentService
                .createAppointment(AppointmentMapper.INSTANCE.appointmentCommandToAppointmentEntity(appointmentCommand), doctorId))), HttpStatus.CREATED);
    }

    @PutMapping("/appointments/cancel/{appointmentId}")
    public HttpStatus cancelAppointment(@PathVariable("appointmentId") Long appointmentId) {
        appointmentService.cancelAppointmentByPatient(appointmentId);
        return HttpStatus.OK;
    }

    @GetMapping("/appointments")
    //    dodac paginacje tutaj
    public ResponseEntity<List<AppointmentDTO>> getPatientAppointments() {
        return new ResponseEntity<>(patientService.getPatientAppointments().stream()
                .map(AppointmentMapper.INSTANCE::appointmentEnityToDTO).toList(), HttpStatus.OK);
    }
}
