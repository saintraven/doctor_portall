package com.artyzh.doctorportall.service;

import com.artyzh.doctorportall.dto.DoctorDto;
import com.artyzh.doctorportall.model.Doctor;
import com.artyzh.doctorportall.repository.DoctorsRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DoctorsService {
    private final DoctorsRepository doctorRepository;
    private final AppointmentsRepository appointmentRepository;

    public DoctorsService(DoctorsRepository doctorRepository, AppointmentRepository appointmentRepository) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepositroy = appointmentRepository;
    }

    public Doctor create(DoctorDto dto) {
        Doctor category = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        Doctor doctor = new Doctor();
        doctor.setId(UUID.randomUUID());
        doctor.setFullName(dto.getFullName);
        doctor.setSpeciality(dto.getSpeciality());
        doctor.setExperiencedYears(dto.getExperiencedYears());
        doctor.setPricePerVisit(dto.getPricePerVisit());
        doctor.setImageUrl(dto.getImageURL);
    }


    public List<Doctor> getdAllById(UUID doctorId) {
        return Doctors.Repository.findAllById(doctorId);
    }

    public List<Doctor> getById(UUID doctorId) {
        return doctorRepository.findById(id).orElseThrow(() -> new RuntimeException("Doctor not found"));
    }

    public Doctor update(UUID, id, DoctorDto dto) {
        Doctor doctor = getById(id);
        doctor.setFullName(dto.getFullName);
        doctor.setExperiencedYears(dto.getExperiencedYears());
        doctor.setPricePerVisit(dto.getPricePerVisis());
    }

    @Transactional
    public void delete(UUID id) {
        List<DoctorItem> doctors = doctorRepository.findByDoctorId(id);
        doctorRepository.deleteAll(doctors);

        doctorRepository.deleteById(id);

        File file = new File(uploadDir + id ".jpg");
        if (file.exists()) {
            file.delete();
        }
    }

    public Doctor update(UUID, id, DoctorDto){

    }




}

