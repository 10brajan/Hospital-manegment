package com.example.zajecia7doktorki.controller;

import com.example.zajecia7doktorki.command.DoctorCommand;
import com.example.zajecia7doktorki.domain.Appointment;
import com.example.zajecia7doktorki.domain.Customer;
import com.example.zajecia7doktorki.domain.Doctor;
import com.example.zajecia7doktorki.dto.AppointmentDoctorDTO;
import com.example.zajecia7doktorki.dto.DoctorDTO;
import com.example.zajecia7doktorki.dto.PatientDTO;
import com.example.zajecia7doktorki.service.AppointmentService;
import com.example.zajecia7doktorki.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/doctors")
public class DoctorController {
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final ModelMapper modelMapper;

    @GetMapping("/getById")
    public ResponseEntity<DoctorDTO> findById() {
        return new ResponseEntity<>(modelMapper.map(doctorService.getDoctor(), DoctorDTO.class), HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<DoctorDTO>> getDoctors() {
        List<Customer> doctors = doctorService.getAllDoctors();
        return new ResponseEntity<>(doctors.stream()
                .map(doctor -> modelMapper
                        .map(doctor, DoctorDTO.class)).toList(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<DoctorDTO> updateDoctor(@RequestBody DoctorCommand doctorCommand) {
        Doctor updatedDoctor = doctorService.updateDoctor(doctorCommand);
        return new ResponseEntity<>(modelMapper.map(updatedDoctor, DoctorDTO.class), HttpStatus.OK);
    }

    @DeleteMapping
    public HttpStatus deleteDoctor() {
        doctorService.deleteDoctor();
        return HttpStatus.OK;
    }

    @PutMapping("/appointments/cancel/{appointmentId}")
    public HttpStatus cancelAppointment(@PathVariable("appointmentId") Long appointmentId) {
        appointmentService.cancelAppointmentByDoctor(appointmentId);
        return HttpStatus.OK;
    }

    @PutMapping("/appointments/success/{appointmentId}")
    public HttpStatus successAppointment(@PathVariable("appointmentId") Long appointmentId) {
        appointmentService.appointmentSuccess(appointmentId);
        return HttpStatus.OK;
    }

    @PutMapping("/patients/changeHealthCondition/{patientId}")
    public HttpStatus changeHealthCondition(@PathVariable("patientId") Long patientId,
            @RequestBody String healthCondition) {
        doctorService.changeHealthCondition(patientId, healthCondition);
        return HttpStatus.OK;
    }

    @GetMapping("/patients")
    public ResponseEntity<Set<PatientDTO>> getDoctorPatients() {
        return new ResponseEntity<>(doctorService.getDoctorPatients().stream()
                .map(patient -> modelMapper.map(patient, PatientDTO.class))
                .collect(Collectors.toSet()), HttpStatus.OK);
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentDoctorDTO>> getDoctorAppointments() {
        List<Appointment> appointments = doctorService.getDoctorAppointments();
        return new ResponseEntity<>(appointments.stream()
                .map(appointment -> modelMapper.map(appointment, AppointmentDoctorDTO.class)).toList(), HttpStatus.OK);
    }
}
