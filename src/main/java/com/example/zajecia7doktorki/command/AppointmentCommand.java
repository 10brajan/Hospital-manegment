package com.example.zajecia7doktorki.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentCommand {
    //    @NotNull(message = "date of the appointment is required")
//    @JsonFormat(pattern = "YYYY-MM-DD")@NotBlank(message = "DATE CAN NOT BE EMPTY OR NULL")
//    @NotBlank(message = "DATE CAN NOT BE EMPTY OR NULL")
//    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @FutureOrPresent(message = "Appointment date cannot be from the past")
    private LocalDate date;

}
