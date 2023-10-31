package com.example.zajecia7doktorki.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 3, max = 20, message = "Your name should contains between 3 to 20 letters")

    private String name;
    @Size(min = 3, max = 20, message = "Your surname should contains between 3 to 20 letters")
    private String surname;
    private int age;
    @Pattern(regexp = "^[0-9]{11}$", message = "PESEL must be at least 11 letters long")
    private String pesel;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> appointments;
}
