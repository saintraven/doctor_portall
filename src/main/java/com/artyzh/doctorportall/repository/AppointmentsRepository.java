package com.artyzh.doctorportall.repository;

import com.artyzh.doctorportall.model.Appointment;
import com.artyzh.doctorportall.dto.AnalyticDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

public interface AppointmentsRepository extends JpaRepository<Appointment, UUID> {
    List<Appointment> getByDoctorId(UUID doctorId);

    List<Appointment> findAppointmentsByAppointmentDate(LocalDateTime appointmentDate);

    @Query(value = """
    select
        d.id as doctorId,
        d.full_name as fullName,
        count(a.id) as totalAppointments,
        count(case when a.status='no_show' then 1 end) as noShowRatePercentage
    from doctors d
    left join appointments a on d.if = a.doctor
    group by d.id, d.full_name
""", nativeQuery = true)
    List<AnalyticDto> getAppointmentsStats();
}
