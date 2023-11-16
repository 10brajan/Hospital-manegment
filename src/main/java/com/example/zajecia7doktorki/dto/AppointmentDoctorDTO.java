package com.example.zajecia7doktorki.dto;

import com.example.zajecia7doktorki.model.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDoctorDTO {
    private LocalDate date;

    private PatientAppointmentDTO patient;

    private Status status;
}
