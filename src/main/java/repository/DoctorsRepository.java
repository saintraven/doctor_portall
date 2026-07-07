package com.example.demo.repository;

import com.example.demo.model.Docotr;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

pubic interface DoctorsRepository extends JpaRepository<Doctor, UUID> {

}

