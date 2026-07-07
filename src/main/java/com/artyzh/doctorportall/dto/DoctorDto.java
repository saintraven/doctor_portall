package com.artyzh.doctorportall.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorDto {
    private String fullName;
    private String speciality;
    private int experienceYears;
    private Number pricePerVisit;

}
