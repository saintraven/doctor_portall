package com.artyzh.doctorportall.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class AnalyticDto {
    private UUID doctorId;
    private String fullName;
    private long totalAppointments;
    private Number utilizationPercentage;
    private BigDecimal totalRevenue;
    private Number noShowRatePercentage;

}
