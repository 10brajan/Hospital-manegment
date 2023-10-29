package com.example.zajecia7doktorki.controller;

import com.example.zajecia7doktorki.command.AppointmentCommand;
import com.example.zajecia7doktorki.command.PatientCommand;
import com.example.zajecia7doktorki.domain.Appointment;
import com.example.zajecia7doktorki.domain.Patient;
import com.example.zajecia7doktorki.dto.AppointmentDTO;
import com.example.zajecia7doktorki.dto.PatientDTO;
import com.example.zajecia7doktorki.service.AppointmentService;
import com.example.zajecia7doktorki.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hospital/patients")
public class PatientController {
    private final PatientService patientService;
    private final AppointmentService appointmentService;
    private final ModelMapper modelMapper;

    @PostMapping("/add")
    public ResponseEntity<PatientDTO> savePatient(@Valid @RequestBody PatientCommand patientCommand) {
        return new ResponseEntity<>(modelMapper
                .map(patientService.createPatient(modelMapper
                        .map(patientCommand, Patient.class)), PatientDTO.class), HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<PatientDTO> findById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(modelMapper.map(patientService.getPatient(id), PatientDTO.class), HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<PatientDTO>> getPatients() {
        List<Patient> patients = patientService.getAllPatients();
        return new ResponseEntity<>(patients.stream()
                .map(patient -> modelMapper
                        .map(patient, PatientDTO.class)).toList(), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable("id") Long id, @RequestBody PatientCommand patientCommand) {
        Patient updatedPatient = patientService.updatePatient(id, patientCommand);
        return new ResponseEntity<>(modelMapper.map(updatedPatient, PatientDTO.class), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public HttpStatus deletePatient(@PathVariable("id") Long id) {
        patientService.deletePatient(id);
        return HttpStatus.OK;
    }

    @PostMapping("/{patientId}/makeAppointment/{doctorId}")
    public ResponseEntity<AppointmentDTO> createAppointment(@PathVariable("patientId") Long patientId, @PathVariable("doctorId") Long doctorId,
                                                            @RequestBody AppointmentCommand appointmentCommand) {

        Appointment appointment = modelMapper.map(appointmentCommand, Appointment.class);

        return new ResponseEntity<>(modelMapper.map(appointmentService.createAppointment(appointment, patientId, doctorId), AppointmentDTO.class), HttpStatus.CREATED);
    }

    @GetMapping("/appointments/{patientId}")
    public ResponseEntity<List<AppointmentDTO>> getPatientAppointments(@PathVariable("patientId") Long patientId) {
        List<Appointment> appointments = patientService.getPatientAppointments(patientId);
        return new ResponseEntity<>(appointments.stream()
                .map(appointment -> modelMapper.map(appointment, AppointmentDTO.class))
                .toList(), HttpStatus.OK);
    }
}
