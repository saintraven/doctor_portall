package com.artyzh.doctorportall.service;

import com.artyzh.doctorportall.dto.AnalyticDto;
import com.artyzh.doctorportall.dto.StatisticsDto;
import com.artyzh.doctorportall.repository.AppointmentsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnalyticsService {
    private final AppointmentsRepository appointmentsRepository;

    public AnalyticsService(AppointmentsRepository appointmentsRepository) {
        this.appointmentsRepository = appointmentsRepository;
    }

    public List<AnalyticDto> getAnalytics() {
        List<StatisticsDto> stats = appointmentsRepository.getAppointmentsStats();
        List<AnalyticDto> result = new ArrayList<>();
        for (StatisticsDto stat : stats) {
            AnalyticDto dto = new AnalyticDto();
            dto.setDoctorId(stat.getDoctorId());
            dto.setFullName(stat.getFullName());
            dto.setTotalRevenue(stat.getTotalRevenue());
            dto.setTotalAppointments(stat.getTotalAppointments());
            dto.setNoShowRatePercentage((double)stat.getTotalNoShow() / stat.getTotalAppointments());
            dto.setDoctorId(stat.getDoctorId());
            result.add(dto);
        }
        return result;
    }
}
