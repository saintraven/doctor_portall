package service;

import org.springframework.stereotype.Service;
import repository.DoctorsRepository;
import repository.AppointmentRepository;
import model.Doctor;
import dto.DoctorDto;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DoctorsService {
    private final DoctorsRepository doctorRepository;
    private final AppointmentRepository appointmentRepositroy;

    public DoctorsService(DoctorsRepository doctorRepository, AppointmentRepository appointmentRepository) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepositroy = appointmentRepository;
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

    public List<Doctor> getdAllById() {
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

}
