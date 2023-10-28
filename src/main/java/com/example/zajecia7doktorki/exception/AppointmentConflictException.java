package com.example.zajecia7doktorki.exception;

public class AppointmentConflictException extends RuntimeException{
    public AppointmentConflictException(String message) {
        super(message);
    }
}
