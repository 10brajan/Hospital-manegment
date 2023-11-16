package com.example.zajecia7doktorki.controller;

import com.example.zajecia7doktorki.command.AppointmentCommand;
import com.example.zajecia7doktorki.command.PatientCommand;
import com.example.zajecia7doktorki.domain.Appointment;
import com.example.zajecia7doktorki.domain.Customer;
import com.example.zajecia7doktorki.domain.Patient;
import com.example.zajecia7doktorki.dto.AppointmentPatientDTO;
import com.example.zajecia7doktorki.dto.PatientDTO;
import com.example.zajecia7doktorki.service.AppointmentService;
import com.example.zajecia7doktorki.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    @GetMapping("/get")
    public ResponseEntity<PatientDTO> findById() {
        return new ResponseEntity<>(modelMapper.map(patientService.getPatient(), PatientDTO.class), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PatientDTO>> getPatients() {
        List<Customer> patients = patientService.getAllPatients();
        return new ResponseEntity<>(patients.stream()
                .map(patient -> modelMapper
                        .map(patient, PatientDTO.class)).toList(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<PatientDTO> updatePatient(@RequestBody PatientCommand patientCommand) {
        Patient updatedPatient = patientService.updatePatient(patientCommand);
        return new ResponseEntity<>(modelMapper.map(updatedPatient, PatientDTO.class), HttpStatus.OK);
    }

    @DeleteMapping
    public HttpStatus deletePatient() {
        patientService.deletePatient();
        return HttpStatus.OK;
    }

    @PutMapping("/makeAppointment/{doctorId}")
    public ResponseEntity<AppointmentPatientDTO> createAppointment(@PathVariable("doctorId") Long doctorId,
                                                            @RequestBody @Valid AppointmentCommand appointmentCommand) {
        return new ResponseEntity<>(modelMapper.map(appointmentService.createAppointment(modelMapper
                        .map(appointmentCommand, Appointment.class), doctorId),
                AppointmentPatientDTO.class), HttpStatus.CREATED);
    }

    @PutMapping("/appointments/cancel/{appointmentId}")
    public HttpStatus cancelAppointment(@PathVariable("appointmentId") Long appointmentId) {
        appointmentService.cancelAppointmentByPatient(appointmentId);
        return HttpStatus.OK;
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentPatientDTO>> getPatientAppointments() {
        List<Appointment> appointments = patientService.getPatientAppointments();
        return new ResponseEntity<>(appointments.stream()
                .map(appointment -> modelMapper.map(appointment, AppointmentPatientDTO.class)).toList(), HttpStatus.OK);
    }
}
