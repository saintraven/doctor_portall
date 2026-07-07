package com.artyzh.doctorportall.repository;

import com.artyzh.doctorportall.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

public interface AppointmentsRepository extends JpaRepository<Appointment, UUID> {
    List<Appointment> getByDoctorId(UUID doctorId);

    List<Appointment> findAppointmentsByAppointmentDate(LocalDateTime appointmentDate);
}
