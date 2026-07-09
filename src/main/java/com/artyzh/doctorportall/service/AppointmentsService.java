package com.artyzh.doctorportall.service;

import com.artyzh.doctorportall.dto.AppointmentDto;
import com.artyzh.doctorportall.model.Appointment;
import com.artyzh.doctorportall.model.Doctor;
import com.artyzh.doctorportall.repository.AppointmentsRepository;
import com.artyzh.doctorportall.repository.DoctorsRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (!appointmentRepository.findAppointmentsByAppointmentDate(dto.getAppointmentDate()).isEmpty())
            throw new RuntimeException("Time slot occupied");
        Doctor doctor = doctorRepository.findById(dto.getDoctorId()).orElseThrow(() -> new RuntimeException("Doctor not found"));
        Appointment appointment = new Appointment();
        appointment.setId(UUID.randomUUID());
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setPatientName(dto.getPatientName());
        appointment.setStatus(dto.getStatus());
        return appointmentRepository.save(appointment);
    }

    @Transactional(readOnly = true)
    public List<Appointment> getAll() {
        return appointmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Appointment getById(UUID appointmentId) {
        return appointmentRepository.findById(appointmentId).orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    // тюнинг под нагрузку: readOnly-транзакция — одна выборка вместо двух автокоммитов,
    // без dirty-check; лишний findById врача (+1 SELECT на 88.9% трафика) заменён
    // на дешёвый existsById и только когда список пуст — непустой список сам доказывает,
    // что врач существует, а 404 для несуществующего врача сохраняется
    @Transactional(readOnly = true)
    public List<Appointment> getByDoctor(UUID doctorId, Pageable pageable) {
        List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId, pageable);
        if (appointments.isEmpty() && !doctorRepository.existsById(doctorId)) {
            throw new RuntimeException("Doctor not found");
        }
        return appointments;
    }

    public Appointment update(UUID id, AppointmentDto dto) {
        if (!appointmentRepository.findAppointmentsByAppointmentDate(dto.getAppointmentDate()).isEmpty())
            throw new RuntimeException("Time slot occupied");
        Doctor doctor = doctorRepository.findById(dto.getDoctorId()).orElseThrow(() -> new RuntimeException("Doctor not found"));
        Appointment appointment = getById(id);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setPatientName(dto.getPatientName());
        appointment.setStatus(dto.getStatus());
        return appointmentRepository.save(appointment);
    }

    public void delete(UUID id) {
        // для исключений
        getById(id);
        appointmentRepository.deleteById(id);
    }
}
