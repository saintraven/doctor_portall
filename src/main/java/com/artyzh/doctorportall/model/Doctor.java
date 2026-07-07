package com.artyzh.doctorportall.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
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
    // добавлено для работы миграции: тип Number заменён на BigDecimal, иначе Hibernate не маппит колонку
    @Column(nullable = false)
    private BigDecimal pricePerVisit;

    private String imageUrl;


}
