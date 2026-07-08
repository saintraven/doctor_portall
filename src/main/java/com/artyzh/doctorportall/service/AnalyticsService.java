package com.artyzh.doctorportall.service;

import com.artyzh.doctorportall.dto.AnalyticDto;
import com.artyzh.doctorportall.repository.AppointmentsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalyticsService {
    private final AppointmentsRepository appointmentsRepository;

    public AnalyticsService(AppointmentsRepository appointmentsRepository) {
        this.appointmentsRepository = appointmentsRepository;
    }

    public List<AnalyticDto> getAnalytics() {
        return appointmentsRepository.getAppointmentsStats();
    }
}
