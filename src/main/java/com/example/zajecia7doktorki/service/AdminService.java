package com.example.zajecia7doktorki.service;

import com.example.zajecia7doktorki.domain.*;
import com.example.zajecia7doktorki.exception.DoctorNotFoundException;
import com.example.zajecia7doktorki.exception.LoginNotFoundException;
import com.example.zajecia7doktorki.model.ActionPerformed;
import com.example.zajecia7doktorki.repository.ActionRepository;
import com.example.zajecia7doktorki.repository.AppointmentRepository;
import com.example.zajecia7doktorki.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final CustomerRepository customerRepository;
    private final AppointmentRepository appointmentRepository;
    private final ActionRepository actionRepository;
    private final CustomerUserDetailsService userDetailsService;
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        customerRepository.delete(customer);

        String username = Optional.ofNullable(userDetailsService.getUserDetails())
                .map(UserDetails::getUsername)
                .orElseThrow(() -> new LoginNotFoundException("Login does not exist"));

        Admin admin = customerRepository.findByLogin(username)
                .filter(Admin.class::isInstance)
                .map(Admin.class::cast)
                .orElseThrow(EntityNotFoundException::new);

        Action action = new Action();
        action.setAdmin(admin);
        action.setActionPerformed(ActionPerformed.REMOVING_CUSTOMER);
        admin.getMadeActions().add(action);
        actionRepository.save(action);
        customerRepository.save(admin);
    }

    public void deleteAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        appointmentRepository.delete(appointment);

        String username = Optional.ofNullable(userDetailsService.getUserDetails())
                .map(UserDetails::getUsername)
                .orElseThrow(() -> new LoginNotFoundException("Login does not exist"));

        Admin admin = customerRepository.findByLogin(username)
                .filter(Admin.class::isInstance)
                .map(Admin.class::cast)
                .orElseThrow(EntityNotFoundException::new);

        Action action = new Action();
        action.setAdmin(admin);
        action.setActionPerformed(ActionPerformed.DELETING_APPOINTMENT);
        admin.getMadeActions().add(action);
        actionRepository.save(action);
        customerRepository.save(admin);
    }

    public void setEnabled(Long id, boolean enabled) {
        Customer customer = customerRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        Action action = new Action();
        action.setOldValue(String.valueOf(customer.getEnabled()));
        action.setNewValue(String.valueOf(enabled));
        action.setChangedField("enabled");
        customer.setEnabled(enabled);
        customerRepository.save(customer);

        String username = Optional.ofNullable(userDetailsService.getUserDetails())
                .map(UserDetails::getUsername)
                .orElseThrow(() -> new LoginNotFoundException("Login does not exist"));

        Admin admin = customerRepository.findByLogin(username)
                .filter(Admin.class::isInstance)
                .map(Admin.class::cast)
                .orElseThrow(EntityNotFoundException::new);

        action.setAdmin(admin);

        action.setActionPerformed(enabled ? ActionPerformed.ENABLING_CUSTOMER : ActionPerformed.DISABLING_CUSTOMER);

        admin.getMadeActions().add(action);
        actionRepository.save(action);
        customerRepository.save(admin);
    }

    public void setLocked(Long id, boolean locked) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        Action action = new Action();
        action.setOldValue(String.valueOf(customer.getLocked()));
        action.setNewValue(String.valueOf(locked));
        action.setChangedField("locked");
        customer.setLocked(locked);
        customerRepository.save(customer);

        String username = Optional.ofNullable(userDetailsService.getUserDetails())
                .map(UserDetails::getUsername)
                .orElseThrow(() -> new LoginNotFoundException("Login does not exist"));

        Admin admin = customerRepository.findByLogin(username)
                .filter(Admin.class::isInstance)
                .map(Admin.class::cast)
                .orElseThrow(EntityNotFoundException::new);

        action.setAdmin(admin);
        action.setActionPerformed(locked ? ActionPerformed.LOCKING_CUSTOMER : ActionPerformed.UNLOCKING_CUSTOMER);


        admin.getMadeActions().add(action);
        actionRepository.save(action);
        customerRepository.save(admin);
    }

    public List<Action> getActions() {
        String username = Optional.ofNullable(userDetailsService.getUserDetails())
                .map(UserDetails::getUsername)
                .orElseThrow(() -> new LoginNotFoundException("Login does not exist"));

        Admin admin = customerRepository.findByLogin(username)
                .filter(Admin.class::isInstance)
                .map(Admin.class::cast)
                .orElseThrow(EntityNotFoundException::new);

        return admin.getMadeActions();

    }
}
