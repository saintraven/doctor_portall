package com.artyzh.doctorportall.controller;

import com.artyzh.doctorportall.dto.AppointmentDto;
import com.artyzh.doctorportall.model.Appointment;
import com.artyzh.doctorportall.service.AppointmentsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentsController {
    private final AppointmentsService appointmentsService;

    public AppointmentsController(AppointmentsService appointmentsService) {
        this.appointmentsService = appointmentsService;
    }

    @PostMapping
    public ResponseEntity<Appointment> create(@RequestBody AppointmentDto appointmentDto) {
        return new ResponseEntity<>(appointmentsService.create(appointmentDto),HttpStatus.CREATED);
    }

    @GetMapping("/{appointment_id}")
    public ResponseEntity<Appointment> getById(@PathVariable("appointment_id") UUID id) {
        return ResponseEntity.ok(appointmentsService.getById(id));
    }

    @PutMapping("/{appointment_id}")
    public ResponseEntity<Appointment> update(@PathVariable("appointment_id") UUID id, @RequestBody AppointmentDto appointmentDto) {
        return ResponseEntity.ok(appointmentsService.update(id, appointmentDto));
    }

    @DeleteMapping("/{appointment_id}")
    public ResponseEntity<Appointment> delete(@PathVariable("appointment_id") UUID id) {
        appointmentsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
