package com.artyzh.doctorportall.repository;

import com.artyzh.doctorportall.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.UUID;
import java.util.List;

public interface AppointmentsRepository extends JpaRepository<Appointment, UUID> {
    // оптимизация SQL запросов - join fetch, чтобы врач доставался тем же запросом, а не отдельным
    @Query("select a from Appointment a join fetch a.doctor where a.doctor.id = :doctorId")
    public List<Appointment> getByDoctorId(@Param("doctorId") UUID doctorId);
}
