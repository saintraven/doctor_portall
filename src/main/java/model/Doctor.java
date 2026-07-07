package com.artyzh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "doctors")
@Getter
@Setter
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


}
