package com.example.zajecia7doktorki.controller;

import com.example.zajecia7doktorki.command.DoctorUpdateCommand;
import com.example.zajecia7doktorki.domain.Customer;
import com.example.zajecia7doktorki.domain.Doctor;
import com.example.zajecia7doktorki.dto.AppointmentDTO;
import com.example.zajecia7doktorki.dto.DoctorDTO;
import com.example.zajecia7doktorki.dto.PatientAppointmentDTO;
import com.example.zajecia7doktorki.mapping.AppointmentMapper;
import com.example.zajecia7doktorki.mapping.DoctorMapper;
import com.example.zajecia7doktorki.mapping.PatientAppointmentMapper;
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

    @GetMapping("/getById")
    public ResponseEntity<DoctorDTO> findById() {
        return new ResponseEntity<>(DoctorMapper.INSTANCE.doctorEnityToDTO(doctorService.getDoctor()), HttpStatus.OK);
    }

    @GetMapping("/getAll")
    //    dodac paginacje tutaj
    public ResponseEntity<List<DoctorDTO>> getDoctors() {
        return new ResponseEntity<>(doctorService.getAllDoctors().stream()
                .map(doctor -> DoctorMapper.INSTANCE.doctorEnityToDTO((Doctor) doctor)).toList(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<DoctorDTO> updateDoctor(@RequestBody DoctorUpdateCommand doctorUpdateCommand) {
        return new ResponseEntity<>(DoctorMapper.INSTANCE.doctorEnityToDTO(doctorService.updateDoctor(doctorUpdateCommand)),
                HttpStatus.OK);
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
//    dodac paginacje tutaj
    public ResponseEntity<Set<PatientAppointmentDTO>> getDoctorPatients() {
        return new ResponseEntity<>(doctorService.getDoctorPatients().stream()
                .map(PatientAppointmentMapper.INSTANCE::patientAppointmentEnityToDTO)
                .collect(Collectors.toSet()), HttpStatus.OK);
    }

    @GetMapping("/appointments")
    //    dodac paginacje tutaj
    public ResponseEntity<List<AppointmentDTO>> getDoctorAppointments() {
        return new ResponseEntity<>(doctorService.getDoctorAppointments().stream()
                .map(AppointmentMapper.INSTANCE::appointmentEnityToDTO).toList(), HttpStatus.OK);
    }
}
