package com.example.zajecia7doktorki.mapping;

import com.example.zajecia7doktorki.command.DoctorCommand;
import com.example.zajecia7doktorki.domain.Doctor;
import com.example.zajecia7doktorki.dto.DoctorDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DoctorMapper {
    DoctorMapper INSTANCE = Mappers.getMapper(DoctorMapper.class);

    DoctorDTO doctorEnityToDTO(Doctor doctor);

    Doctor doctorCommandToDoctorEntity(DoctorCommand doctorCommand);
}