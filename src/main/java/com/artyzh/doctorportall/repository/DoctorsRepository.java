package com.artyzh.doctorportall.repository;

import com.artyzh.doctorportall.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface DoctorsRepository extends JpaRepository<Doctor, UUID> {

}

