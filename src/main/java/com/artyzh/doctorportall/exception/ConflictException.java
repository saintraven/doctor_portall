package com.artyzh.doctorportall.exception;

// тюнинг под нагрузку: занятый слот — это конфликт состояния (409), а не 400;
// возникает из нарушения uq_appointments_doctor_slot при конкурентной вставке
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
