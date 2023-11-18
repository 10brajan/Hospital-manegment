package com.example.zajecia7doktorki.service;

import com.example.zajecia7doktorki.command.AdminUpdateCommand;
import com.example.zajecia7doktorki.domain.Action;
import com.example.zajecia7doktorki.domain.Admin;
import com.example.zajecia7doktorki.domain.Appointment;
import com.example.zajecia7doktorki.domain.Customer;
import com.example.zajecia7doktorki.exception.AdminNotFoundException;
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
    private static final String LOGIN_DOES_NOT_EXIST = "Login does not exist";

    private static final String ADMIN_WITH_THIS_LOGIN_DOES_NOT_EXIST = "Admin with this login does not exist";

    private final CustomerRepository customerRepository;

    private final AppointmentRepository appointmentRepository;

    private final ActionRepository actionRepository;

    private final CustomerUserDetailsService userDetailsService;


    public Admin updateAdmin(AdminUpdateCommand adminUpdateCommand) {
        String username = getUsername();
        Admin adminToUpdate = getAdmin(username);

        Optional.ofNullable(adminUpdateCommand.getName()).ifPresent(adminToUpdate::setName);
        Optional.ofNullable(adminUpdateCommand.getSurname()).ifPresent(adminToUpdate::setSurname);
        Optional.of(adminUpdateCommand.getAge()).filter(age -> age > 0).ifPresent(adminToUpdate::setAge);
        return customerRepository.save(adminToUpdate);
    }

    private Admin getAdmin(String username) {
        return customerRepository.findByLogin(username)
                .filter(Admin.class::isInstance)
                .map(Admin.class::cast)
                .orElseThrow(() -> new AdminNotFoundException(ADMIN_WITH_THIS_LOGIN_DOES_NOT_EXIST));
    }

    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        customerRepository.delete(customer);

        String username = getUsername();
        Admin admin = getAdmin(username);

        Action action = preparedActionObject(ActionPerformed.REMOVING_CUSTOMER);
        admin.getMadeActions().add(action);

        actionRepository.save(action);
        customerRepository.save(admin);
    }

    public void deleteAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        appointmentRepository.delete(appointment);

        String username = getUsername();

        Admin admin = getAdmin(username);

        Action action = preparedActionObject(ActionPerformed.DELETING_APPOINTMENT);

        admin.getMadeActions().add(action);
        actionRepository.save(action);
        customerRepository.save(admin);
    }

    public void setEnabled(Long id, boolean enabled) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        Action action = preparedActionObject(enabled, customer, "enabled");
        customer.setEnabled(enabled);

        String username = getUsername();
        Admin admin = getAdmin(username);

        action.setAdmin(admin);

        action.setActionPerformed(enabled ? ActionPerformed.ENABLING_CUSTOMER : ActionPerformed.DISABLING_CUSTOMER);

        admin.getMadeActions().add(action);
        customerRepository.save(customer);
        actionRepository.save(action);
        customerRepository.save(admin);
    }

    private String getUsername() {
        return Optional.ofNullable(userDetailsService.getUserDetails())
                .map(UserDetails::getUsername)
                .orElseThrow(() -> new LoginNotFoundException(LOGIN_DOES_NOT_EXIST));
    }

    public void setLocked(Long id, boolean locked) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        Action action = preparedActionObject(locked, customer, "locked");
        customer.setLocked(locked);
        String username = getUsername();
        Admin admin = getAdmin(username);

        action.setActionPerformed(locked ? ActionPerformed.LOCKING_CUSTOMER : ActionPerformed.UNLOCKING_CUSTOMER);

        admin.getMadeActions().add(action);
        customerRepository.save(customer);
        actionRepository.save(action);
        customerRepository.save(admin);
    }

    private static Action preparedActionObject(boolean actionPerformed, Customer customer, String attribute) {
        Action action = new Action();
        action.setOldValue(String.valueOf(customer.getLocked()));
        action.setNewValue(String.valueOf(actionPerformed));
        action.setChangedField(attribute);
        return action;
    }

    private static Action preparedActionObject(ActionPerformed actionPerformed) {
        Action action = new Action();
        action.setActionPerformed(actionPerformed);
        return action;
    }

    public List<Action> getActions() {
        String username = getUsername();
        Admin admin = getAdmin(username);

        return admin.getMadeActions();
    }
}
