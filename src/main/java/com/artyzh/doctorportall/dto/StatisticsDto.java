package com.artyzh.doctorportall.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class StatisticsDto {
    private UUID doctorId;
    private String fullName;
    private long totalAppointments;
    private BigDecimal totalRevenue;
    private long totalNoShow;

    public StatisticsDto(UUID doctorId, String fullName, long totalAppointments, BigDecimal totalRevenue, long totalNoShow) {
        this.doctorId = doctorId;
        this.fullName = fullName;
        this.totalAppointments = totalAppointments;
        this.totalRevenue = totalRevenue;
        this.totalNoShow = totalNoShow;
    }
}
