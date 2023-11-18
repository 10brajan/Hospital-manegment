package com.example.zajecia7doktorki.mapping;

import com.example.zajecia7doktorki.domain.Action;
import com.example.zajecia7doktorki.dto.ActionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ActionMapper {
    ActionMapper INSTANCE = Mappers.getMapper(ActionMapper.class);

    ActionDTO actionEnityToDTO(Action action);
}
