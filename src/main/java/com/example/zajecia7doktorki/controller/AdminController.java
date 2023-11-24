package com.example.zajecia7doktorki.controller;

import com.example.zajecia7doktorki.command.AdminUpdateCommand;
import com.example.zajecia7doktorki.dto.ActionDTO;
import com.example.zajecia7doktorki.dto.AdminDTO;
import com.example.zajecia7doktorki.mapping.ActionMapper;
import com.example.zajecia7doktorki.mapping.AdminMapper;
import com.example.zajecia7doktorki.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Admin controller", description = "Thanks to hospital api we can create a new admin")
@RequestMapping("/api/v1/admins")
public class AdminController {

    private final AdminService adminService;
    private final AdminMapper adminMapper;
    private final ActionMapper actionMapper;

    @GetMapping("/get")
    @Operation(summary = "Get admin details", responses = {
            @ApiResponse(description = "Details of the admin", responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AdminDTO.class)))})
    public ResponseEntity<AdminDTO> getAdmin() {
        return new ResponseEntity<>(adminMapper.adminEntityToDTO(adminService.getAdmin()), HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<Page<AdminDTO>> getAdmins(@PageableDefault Pageable pageable) {
        return new ResponseEntity<>(adminService.getAdmins(pageable)
                .map(adminMapper::adminEntityToDTO), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<AdminDTO> updateAdmin(@RequestBody AdminUpdateCommand adminUpdateCommand) {
        return new ResponseEntity<>(adminMapper.adminEntityToDTO(adminService.updateAdmin(adminUpdateCommand)), HttpStatus.OK);
    }

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
    //    dodac paginacje tutaj
    public ResponseEntity<Page<ActionDTO>> displayActions(@PageableDefault Pageable pageable) {
        return new ResponseEntity<>(adminService.getActions(pageable)
                .map(actionMapper::actionEntityToDTO), HttpStatus.OK);
    }
}
