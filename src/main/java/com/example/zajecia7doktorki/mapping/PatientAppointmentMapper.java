package com.example.zajecia7doktorki.mapping;

import com.example.zajecia7doktorki.domain.Patient;
import com.example.zajecia7doktorki.dto.PatientAppointmentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PatientAppointmentMapper {
    PatientAppointmentMapper INSTANCE = Mappers.getMapper(PatientAppointmentMapper.class);

    PatientAppointmentDTO patientAppointmentEnityToDTO(Patient patient);
}
