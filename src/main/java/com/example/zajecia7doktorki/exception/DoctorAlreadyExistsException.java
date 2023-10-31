package com.example.zajecia7doktorki.exception;

public class DoctorAlreadyExistsException extends RuntimeException{
    public DoctorAlreadyExistsException(String message) {
        super(message);
    }
}
