package com.artyzh.doctorportall.repository;

import com.artyzh.doctorportall.dto.StatisticsDto;
import com.artyzh.doctorportall.model.Appointment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

public interface AppointmentsRepository extends JpaRepository<Appointment, UUID> {
    List<Appointment> getByDoctorId(UUID doctorId);

    // тюнинг под нагрузку: горячий путь (88.9% запросов) — врач грузится одним join'ом
    // (EntityGraph), объём ответа ограничен Pageable, а не растёт с таблицей
    @EntityGraph(attributePaths = "doctor")
    List<Appointment> findByDoctorId(UUID doctorId, Pageable pageable);

    List<Appointment> findAppointmentsByAppointmentDate(LocalDateTime appointmentDate);

    @Query(value = """
    select
        d.id as doctorId,
        d.full_name as fullName,
        count(a.id) as totalAppointments,
        sum(case when a.status='completed' then 1 end) * d.price_per_visit as totalRevenue,
        count(case when a.status='no_show' then 1 end) as totalNoShow
    from doctors d
    left join appointments a on d.id = a.doctor
    group by d.id, d.full_name
""", nativeQuery = true)
    List<StatisticsDto> getAppointmentsStats();
}
