package com.artyzh.doctorportall.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AnalyticDto {
    private UUID doctorId;
    private String fullName;
    private int totalAppointments;
    private Number utilizationPercentage;
    private Number totalRevenue;
    private Number noShowRatePercentage;

}
