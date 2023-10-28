package com.example.zajecia7doktorki.command;

import com.example.zajecia7doktorki.domain.Appointment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorCommand {

    private String name;
    private String surname;
    private String specialization;
    private int age;

}
