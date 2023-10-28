package com.example.zajecia7doktorki.controller;

import com.example.zajecia7doktorki.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app")
public class AppointmentController {
    private final AppointmentService appointmentService;

}
