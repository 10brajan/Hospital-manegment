package com.example.zajecia7doktorki.controller;

import com.example.zajecia7doktorki.domain.Action;
import com.example.zajecia7doktorki.dto.ActionDTO;
import com.example.zajecia7doktorki.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admins")
public class AdminController {

    private final AdminService adminService;
    private final ModelMapper modelMapper;

    @DeleteMapping("/customer/{id}")
    public HttpStatus deleteCustomer(@PathVariable("id") Long id) {
        adminService.deleteCustomer(id);
        return HttpStatus.OK;
    }

    @DeleteMapping("/appointment/{id}")
    public HttpStatus deleteAppointment(@PathVariable("id") Long id) {
        adminService.deleteAppointment(id);
        return HttpStatus.OK;
    }

    @PutMapping("/lock/{id}")
    public HttpStatus lockCustomer(@PathVariable("id") Long id) {
        adminService.setLocked(id, true);
        return HttpStatus.OK;
    }

    @PutMapping("/unlock/{id}")
    public HttpStatus unlockCustomer(@PathVariable("id") Long id) {
        adminService.setLocked(id, false);
        return HttpStatus.OK;
    }

    @PutMapping("/disable/{id}")
    public HttpStatus disableCustomer(@PathVariable("id") Long id) {
        adminService.setEnabled(id, false);
        return HttpStatus.OK;
    }

    @PutMapping("/enable/{id}")
    public HttpStatus enableCustomer(@PathVariable("id") Long id) {
        adminService.setEnabled(id, true);
        return HttpStatus.OK;
    }

    @GetMapping("/actions")
    public ResponseEntity<List<ActionDTO>> displayActions() {
        List<Action> actions = adminService.getActions();
        return new ResponseEntity<>(actions.stream()
                .map(action -> modelMapper.map(action, ActionDTO.class)).toList() , HttpStatus.OK);
    }
}
