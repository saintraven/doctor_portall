package com.artyzh.doctorportall.repository;

import com.artyzh.doctorportall.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    // добавлено для работы миграции: исправлена опечатка (было "UUId, id" — не компилировалось)
    List<Appointment> findAppointmentById(UUID id);
}