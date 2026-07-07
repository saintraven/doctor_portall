package com.artyzh.doctorportall.service;

import com.artyzh.doctorportall.dto.AppointmentDto;
import com.artyzh.doctorportall.model.Appointment;
import com.artyzh.doctorportall.repository.AppointmentsRepository;
import com.artyzh.doctorportall.repository.DoctorsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AppointmentsService {
    private final DoctorsRepository doctorRepository;
    private final AppointmentsRepository appointmentRepository;

    public AppointmentsService(DoctorsRepository doctorRepository, AppointmentsRepository appointmentRepository) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public Appointment create(AppointmentDto dto) {
        // TODO: checker of collision
        Appointment appointment = new Appointment();
        appointment.setId(UUID.randomUUID());
        appointment.setDoctor(doctorRepository.getById(dto.getDoctorId()));
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setPatientName(dto.getPatientName());
        appointment.setStatus(dto.getStatus());
        return appointment;
    }

    public List<Appointment> getdAllById() {
        return appointmentRepository.findAll();
    }

    public Appointment getById(UUID appointmentId) {
        return appointmentRepository.findById(appointmentId).orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    public List<Appointment> getByDoctor(UUID doctorId) {
        return appointmentRepository.getByDoctorId(doctorId);
    }

    public Appointment update(UUID id, AppointmentDto dto) {
        Appointment appointment = getById(id);
        appointment.setDoctor(doctorRepository.getById(dto.getDoctorId()));
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setPatientName(dto.getPatientName());
        appointment.setStatus(dto.getStatus());
        return appointmentRepository.save(appointment);
    }

    public void delete(UUID id) {
        appointmentRepository.deleteById(id);
    }
}
