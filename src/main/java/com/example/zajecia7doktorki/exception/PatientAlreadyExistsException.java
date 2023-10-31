package com.example.zajecia7doktorki.exception;

public class PatientAlreadyExistsException extends RuntimeException{
    public PatientAlreadyExistsException(String message) {
        super(message);
    }
}
