package com.example.zajecia7doktorki.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorUpdateCommand {
    @Size(min = 3, max = 20, message = "Your name should contains between 3 to 20 letters")
    private String name;

    @Size(min = 3, max = 20, message = "Your surname should contains between 3 to 20 letters")
    private String surname;

    @Min(value = 25, message = "To be a doctor your age must be higher than 25")
    private int age;

    private String specialization;
}
