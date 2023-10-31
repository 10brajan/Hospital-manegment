package com.example.zajecia7doktorki.command;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientCommand {
    @Size(min = 3, max = 20, message = "Your name should contains between 3 to 20 letters")
    private String name;
    @Size(min = 3, max = 20, message = "Your surname should contains between 3 to 20 letters")
    private String surname;
    private int age;
    @Pattern(regexp = "^[0-9]{11}$", message = "PESEL must be at least 11 letters long")
    private String pesel;
}
