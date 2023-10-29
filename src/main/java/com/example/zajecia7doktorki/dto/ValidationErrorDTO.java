package com.example.zajecia7doktorki.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ValidationErrorDTO {
    private Map<String, String> errors;

}
