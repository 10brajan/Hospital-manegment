package com.example.zajecia7doktorki.mapping;

import com.example.zajecia7doktorki.command.AdminCommand;
import com.example.zajecia7doktorki.domain.Admin;
import com.example.zajecia7doktorki.dto.AdminDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminMapper {

    AdminMapper INSTANCE = Mappers.getMapper(AdminMapper.class);

//    @Mapping(target = "chuj", source = "kolba")
    AdminDTO adminEnityToDTO(Admin admin);

    Admin adminCommandToAdminEntity(AdminCommand adminCommand);

}
