package com.artyzh.doctorportall.service;

import com.artyzh.doctorportall.model.Appointment;
import org.springframework.stereotype.Service;
import com.artyzh.doctorportall.repository.DoctorsRepository;
import com.artyzh.doctorportall.repository.AppointmentsRepository;
import com.artyzh.doctorportall.model.Doctor;
import com.artyzh.doctorportall.dto.DoctorDto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import java.util.List;
import java.util.UUID;

@Service
public class DoctorsService {
    private final DoctorsRepository doctorRepository;
    private final AppointmentsRepository appointmentRepository;
    private final String uploadDir = "uploads/";

    public DoctorsService(DoctorsRepository doctorRepository, AppointmentsRepository appointmentRepository) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public Doctor create(DoctorDto dto) {
        Doctor doctor = new Doctor();
        doctor.setId(UUID.randomUUID());
        doctor.setFullName(dto.getFullName());
        doctor.setSpeciality(dto.getSpeciality());
        doctor.setExperiencedYears(dto.getExperienceYears());
        doctor.setPricePerVisit(dto.getPricePerVisit());
        return doctor;
    }

    public List<Doctor> getAll() {
        return doctorRepository.findAll();
    }

    public Doctor getById(UUID doctorId) {
        return doctorRepository.findById(doctorId).orElseThrow(() -> new RuntimeException("Doctor not found"));
    }

    public Doctor update(UUID id, DoctorDto dto) {
        Doctor doctor = getById(id);
        doctor.setFullName(dto.getFullName());
        doctor.setExperiencedYears(dto.getExperienceYears());
        doctor.setPricePerVisit(dto.getPricePerVisit());
        return doctorRepository.save(doctor);
    }

    @Transactional
    public void delete(UUID id) {
        List<Doctor> doctors = doctorRepository.findByID(id);
        doctorRepository.deleteAll(doctors);

        doctorRepository.deleteById(id);

        File file = new File(uploadDir + id, ".jpg");
        if (file.exists()) {
            file.delete();
        }
    }

    public void uploadImage(UUID id, byte[] imageBytes) throws IOException {
        Doctor doctor = getById(id);
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, id + ".jpg");
        try (FileOutputStream fos = FileOutputStream(file)){
            fos.write(imageBytes);
        }

        doctor.setImageUrl("/api/v1/doctors/" + id + "/image");
        doctorRepository.save(doctor);
    }

    public byte[] getImage(UUID id) throws IOException {
        File file = new File(uploadDir + id + ".jpg");
        if (!file.exists()) {
            throw new RuntimeException("Image not found");
        }
        return Files.readAllBytes(file.toPath());
    }
}

