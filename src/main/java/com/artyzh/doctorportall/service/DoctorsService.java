package com.artyzh.doctorportall.service;

import com.artyzh.doctorportall.exception.NotFoundException;
import com.artyzh.doctorportall.model.Appointment;
// тюнинг под нагрузку: jakarta.transaction.Transactional импортировался, но нигде не использовался;
// читающие методы переведены на spring-овый @Transactional(readOnly = true)
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    // тюнинг под нагрузку: код писал в "images/", а том в compose смонтирован
    // в /app/uploads — картинки не попадали в том и теряли смысл volume
    private final Path uploadDir = Path.of("uploads");

    public DoctorsService(DoctorsRepository doctorRepository, AppointmentsRepository appointmentRepository) throws IOException {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        // тюнинг под нагрузку: каталог создаётся один раз при старте,
        // а не Files.createDirectories на каждый PUT
        Files.createDirectories(uploadDir);
    }

    public Doctor create(DoctorDto dto) {
        Doctor doctor = new Doctor();
        doctor.setId(UUID.randomUUID());
        doctor.setFullName(dto.getFullName());
        doctor.setSpeciality(dto.getSpeciality());
        doctor.setExperiencedYears(dto.getExperienceYears());
        doctor.setPricePerVisit(dto.getPricePerVisit());
        return doctorRepository.save(doctor);
    }

    @Transactional(readOnly = true)
    public List<Doctor> getAll() {
        return doctorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Doctor getById(UUID doctorId) {
        return doctorRepository.findById(doctorId).orElseThrow(() -> new NotFoundException("Doctor not found"));
    }

    public Doctor update(UUID id, DoctorDto dto) {
        Doctor doctor = getById(id);
        doctor.setFullName(dto.getFullName());
        doctor.setExperiencedYears(dto.getExperienceYears());
        doctor.setPricePerVisit(dto.getPricePerVisit());
        return doctorRepository.save(doctor);
    }

    // добавлено для работы миграции
    public void delete(UUID id) throws IOException {
        // для исключений
        getById(id);
        List<Appointment> appointments = appointmentRepository.getByDoctorId(id);
        appointmentRepository.deleteAll(appointments);
        doctorRepository.deleteById(id);

        // тюнинг под нагрузку: new File(uploadDir + id, ".jpg") — это конструктор
        // (parent, child), он давал путь uploads/{id}/.jpg и расширение .jpg,
        // при том что запись идёт в {id}.png — файлы никогда не удалялись
        Files.deleteIfExists(uploadDir.resolve(id + ".png"));
    }

    // добавлено для работы миграции
    public void uploadImage(UUID id, byte[] image) throws IOException {
        Doctor doctor = getById(id);
        Path imageFile = uploadDir.resolve(doctor.getId() + ".png");
        Files.write(imageFile, image);
        doctor.setImageUrl(imageFile.toString());
        doctorRepository.save(doctor);
    }

    // добавлено для работы миграции
    public byte[] getImage(UUID id) throws IOException {
        Doctor doctor = getById(id);
        if (doctor.getImageUrl() == null) {
            throw new NotFoundException("Image not found");
        }
        return Files.readAllBytes(Path.of(doctor.getImageUrl()));
    }

}

