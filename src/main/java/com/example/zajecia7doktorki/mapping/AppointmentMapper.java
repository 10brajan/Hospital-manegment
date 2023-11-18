package com.example.zajecia7doktorki.mapping;

import com.example.zajecia7doktorki.command.AppointmentCommand;
import com.example.zajecia7doktorki.command.DoctorCommand;
import com.example.zajecia7doktorki.domain.Appointment;
import com.example.zajecia7doktorki.domain.Doctor;
import com.example.zajecia7doktorki.dto.AppointmentDTO;
import com.example.zajecia7doktorki.dto.DoctorDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AppointmentMapper {
    AppointmentMapper INSTANCE = Mappers.getMapper(AppointmentMapper.class);

    AppointmentDTO appointmentEnityToDTO(Appointment appointment);

    Appointment appointmentCommandToAppointmentEntity(AppointmentCommand appointmentCommand);
}
