package com.artyzh.doctorportall.repository;

import com.artyzh.doctorportall.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface AppointmentsRepository extends JpaRepository<Appointment, UUID> {

}
