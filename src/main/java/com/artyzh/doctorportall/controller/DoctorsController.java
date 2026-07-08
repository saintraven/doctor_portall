package com.artyzh.doctorportall.controller;

import com.artyzh.doctorportall.model.Appointment;
import com.artyzh.doctorportall.model.Doctor;
import com.artyzh.doctorportall.dto.DoctorDto;
import com.artyzh.doctorportall.service.AppointmentsService;
import com.artyzh.doctorportall.service.DoctorsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/doctors")
public class DoctorsController {
    private final AppointmentsService appointmentsService;
    private final DoctorsService doctorsService;

    public DoctorsController(AppointmentsService appointmentsService, DoctorsService doctorsService) {
        this.appointmentsService = appointmentsService;
        this.doctorsService = doctorsService;
    }

    @PostMapping
    public ResponseEntity<Doctor> create(@RequestBody DoctorDto doctorDto) {
        return new ResponseEntity<>(doctorsService.create(doctorDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Doctor>> getAll() {
        return ResponseEntity.ok(doctorsService.getAll());
    }

    @GetMapping("/{doctor_id}")
    public ResponseEntity<Doctor> getById(@PathVariable("doctor_id") UUID id) {
        return ResponseEntity.ok(doctorsService.getById(id));
    }

    @PutMapping("/{doctor_id}")
    public ResponseEntity<Doctor> update(@PathVariable("doctor_id") UUID id, @RequestBody DoctorDto doctorDto) {
        return ResponseEntity.ok(doctorsService.update(id, doctorDto));
    }

    @DeleteMapping("/{doctor_id}")
    public ResponseEntity<Void> delete(@PathVariable("doctor_id") UUID id) {
        doctorsService.delete(id); // для исключений, 404 при отсутствии врача теперь откидывает GlobalExceptionHandler
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{doctor_id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadImage(@PathVariable("doctor_id") UUID id, @RequestParam("file") MultipartFile file) throws IOException {
        doctorsService.uploadImage(id, file.getBytes());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{doctor_id}/image")
    public void getImage(@PathVariable("doctor_id") UUID id, HttpServletResponse response) throws IOException {
        byte[] image = doctorsService.getImage(id);

        response.setContentType("image/png");

        response.getOutputStream().write(image);
        response.getOutputStream().flush();
    }

    @GetMapping("/{doctor_id}/appointments")
    public ResponseEntity<List<Appointment>> getAppointments(@PathVariable("doctor_id") UUID id) {
        return ResponseEntity.ok(appointmentsService.getByDoctor(id));
    }
}
