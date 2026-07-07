package com.artyzh.doctorportall.service;

import com.artyzh.doctorportall.model.Appointment;
import org.springframework.stereotype.Service;
import com.artyzh.doctorportall.repository.DoctorsRepository;
import com.artyzh.doctorportall.repository.AppointmentsRepository;
import com.artyzh.doctorportall.model.Doctor;
import com.artyzh.doctorportall.dto.DoctorDto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Service
public class DoctorsService {
    private final DoctorsRepository doctorRepository;
    private final AppointmentsRepository appointmentRepository;

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

    // добавлено для работы миграции
    public void delete(UUID id) {
        doctorRepository.deleteById(id);
    }

    // добавлено для работы миграции
    public void saveImage(UUID id, byte[] image) throws IOException {
        Doctor doctor = getById(id);
        Path imageFile = Path.of("images", doctor.getId() + ".png");
        Files.createDirectories(imageFile.getParent());
        Files.write(imageFile, image);
        doctor.setImageUrl(imageFile.toString());
        doctorRepository.save(doctor);
    }

    // добавлено для работы миграции
    public byte[] getImage(UUID id) throws IOException {
        Doctor doctor = getById(id);
        if (doctor.getImageUrl() == null) {
            throw new RuntimeException("Image not found");
        }
        return Files.readAllBytes(Path.of(doctor.getImageUrl()));
    }

}
