package com.artyzh.doctorportall.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "doctors")
public class Doctor {
    @Id
    private UUID id;
    @Column(nullable = false)
    private String fullName;
    @Column(nullable = false)
    private String speciality;
    @Column(nullable = false)
    private int experiencedYears;
    @Column(nullable = false)
    private Number pricePerVisit;

    private String imageUrl;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public int getExperiencedYears() {
        return experiencedYears;
    }

    public void setExperiencedYears(int experiencedYears) {
        this.experiencedYears = experiencedYears;
    }

    public Number getPricePerVisit() {
        return pricePerVisit;
    }

    public void setPricePerVisit(Number pricePerVisit) {
        this.pricePerVisit = pricePerVisit;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
