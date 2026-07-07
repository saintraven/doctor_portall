package com.artyzh.doctorportall.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DoctorDto {
    private String fullName;
    private String speciality;
    private int experienceYears;
    // добавлено для работы миграции (BigDecimal как в Doctor)
    private BigDecimal pricePerVisit;

}
