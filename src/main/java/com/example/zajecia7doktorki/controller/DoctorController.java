package com.example.zajecia7doktorki.controller;

import com.example.zajecia7doktorki.command.DoctorUpdateCommand;
import com.example.zajecia7doktorki.dto.AppointmentDTO;
import com.example.zajecia7doktorki.dto.DoctorDTO;
import com.example.zajecia7doktorki.dto.PatientAppointmentDTO;
import com.example.zajecia7doktorki.mapping.AppointmentMapper;
import com.example.zajecia7doktorki.mapping.DoctorMapper;
import com.example.zajecia7doktorki.mapping.PatientMapper;
import com.example.zajecia7doktorki.service.AppointmentService;
import com.example.zajecia7doktorki.service.DoctorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Tag(name = "Doctor controller", description = "Thanks to hospital api we can create a new doctor")
@RequestMapping("/api/v1/doctors")
public class DoctorController {
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final DoctorMapper doctorMapper;
    private final PatientMapper patientMapper;
    private final AppointmentMapper appointmentMapper;

    @GetMapping("/get")
    public ResponseEntity<DoctorDTO> getDoctor() {
        return new ResponseEntity<>(doctorMapper.doctorEntityToDTO(doctorService.getDoctor()), HttpStatus.OK);
    }

    @GetMapping("/getAll")
    //    dodac paginacje tutaj
    public ResponseEntity<Page<DoctorDTO>> getDoctors(@PageableDefault Pageable pageable) {
        return new ResponseEntity<>(doctorService.getAllDoctors(pageable)
                .map(doctorMapper::doctorEntityToDTO), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<DoctorDTO> updateDoctor(@RequestBody DoctorUpdateCommand doctorUpdateCommand) {
        return new ResponseEntity<>(doctorMapper.doctorEntityToDTO(doctorService.updateDoctor(doctorUpdateCommand)),
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
    public ResponseEntity<Page<PatientAppointmentDTO>> getDoctorPatients(@PageableDefault Pageable pageable) {
        return new ResponseEntity<>(doctorService.getDoctorPatients(pageable)
                .map(patientMapper::patientEntityToPatientAppointmentDTO), HttpStatus.OK);
    }

    @GetMapping("/appointments")
    //    dodac paginacje tutaj
    public ResponseEntity<Page<AppointmentDTO>> getDoctorAppointments(@PageableDefault Pageable pageable) {
        return new ResponseEntity<>(doctorService.getDoctorAppointments(pageable)
                .map(appointmentMapper::appointmentEntityToDTO), HttpStatus.OK);
    }
}
