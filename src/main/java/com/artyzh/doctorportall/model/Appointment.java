package com.artyzh.doctorportall.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "appointments")
@Getter
@Setter
public class Appointment {
    @Id
    private UUID id;
    // тюнинг под нагрузку: EAGER -> LAZY, чтобы врач не тянулся отдельным селектом везде,
    // где он не нужен; на горячем GET он подгружается одним join'ом через @EntityGraph
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor", nullable = false)
    private Doctor doctor;
    @Column(nullable = false)
    private String patientName;
    @Column(nullable = false)
    private LocalDateTime appointmentDate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

}
