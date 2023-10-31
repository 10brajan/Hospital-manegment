package com.example.zajecia7doktorki.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 3, max = 20, message = "Your name should contains between 3 to 20 letters")
    private String name;
    @Size(min = 3, max = 20, message = "Your name should contains between 3 to 20 letters")
    private String surname;
    private String specialization;
    @Min(value = 25, message = "To be a doctor your age must be higher than 25")
    private int age;
    @Pattern(regexp = "^[0-9]{11}$", message = "PESEL must be at least 11 letters long")
    private String pesel;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> appointments;
}
