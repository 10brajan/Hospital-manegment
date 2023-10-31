package com.example.zajecia7doktorki.command;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorCommand {

    private String name;
    private String surname;
    private String specialization;
    private int age;
    private String pesel;

}
