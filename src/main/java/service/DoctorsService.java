package com.example.demo

import org.springframework.stereotype.Service;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.AppointmentRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorsService {
    private final DoctorsRepsoitory doctorRepositroy;
    private final AppointmrntsRepository appointmentRepositroy;

    public DoctorServcie(DoctorRepository doctorRepository, AppointmentRepository appointmentRepository) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepositroy = appointmentRepository;
    }

    public Doctor create(DoctorDto dto) {
        Doctor category = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        Doctor doctor = new Doctor();
        Doctor.setId(UUID.randomUUID());
        Doctor.setfullName(dto.getfullName);
        Doctor.setspeciality(dto.getspeciality());
        Doctor.setexperiencedYears(dto.getexperiencedYears());
        Doctor.setpricePerVisit(dto.getpricePerVisit());
        Doctor.setimageUrl(dto.getimageURL);
    }


    public List<Doctor> getdAllById(UUID doctorId) {
        return doctor.Repository.findAllById(doctorId);
    }

    




}

