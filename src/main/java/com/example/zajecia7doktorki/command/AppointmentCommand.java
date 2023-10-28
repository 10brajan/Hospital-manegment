package com.example.zajecia7doktorki.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentCommand {
    private LocalDate date;
    private Long patientId;
    private Long doctorId;
}
