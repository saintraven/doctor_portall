package com.artyzh.doctorportall.service;

import com.artyzh.doctorportall.dto.AppointmentDto;
import com.artyzh.doctorportall.exception.NotFoundException;
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

    // тюнинг под нагрузку: проверка "слот занят" была (а) глобальной по всем врачам —
    // два разных врача не могли принимать в одну секунду, (б) check-then-act селектом,
    // который под конкурентной нагрузкой всё равно не гарантирует инвариант,
    // (в) материализовала список сущностей (с EAGER-врачом) ради isEmpty().
    // Теперь инвариант держит уникальный индекс uq_appointments_doctor_slot,
    // нарушение ловится в GlobalExceptionHandler как 409. findById врача остаётся:
    // он же нужен для сериализации ответа (вложенный doctor)
    public Appointment create(AppointmentDto dto) {
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new NotFoundException("Doctor not found"));
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
        return appointmentRepository.findById(appointmentId).orElseThrow(() -> new NotFoundException("Appointment not found"));
    }

    // тюнинг под нагрузку: readOnly-транзакция — одна выборка вместо двух автокоммитов,
    // без dirty-check; лишний findById врача (+1 SELECT на 88.9% трафика) заменён
    // на дешёвый existsById и только когда список пуст — непустой список сам доказывает,
    // что врач существует, а 404 для несуществующего врача сохраняется
    @Transactional(readOnly = true)
    public List<Appointment> getByDoctor(UUID doctorId, Pageable pageable) {
        List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId, pageable);
        if (appointments.isEmpty() && !doctorRepository.existsById(doctorId)) {
            throw new NotFoundException("Doctor not found");
        }
        return appointments;
    }

    // тюнинг под нагрузку: та же замена check-then-act на уникальный индекс, что и в create
    public Appointment update(UUID id, AppointmentDto dto) {
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new NotFoundException("Doctor not found"));
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
