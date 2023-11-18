package com.example.zajecia7doktorki.mapping;

import com.example.zajecia7doktorki.command.DoctorCommand;
import com.example.zajecia7doktorki.command.PatientCommand;
import com.example.zajecia7doktorki.domain.Doctor;
import com.example.zajecia7doktorki.domain.Patient;
import com.example.zajecia7doktorki.dto.PatientDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PatientMapper {
    PatientMapper INSTANCE = Mappers.getMapper(PatientMapper.class);

    PatientDTO patientEnityToDTO(Patient patient);
    Patient patientCommandToPatientEntity(PatientCommand patientCommand);


}
