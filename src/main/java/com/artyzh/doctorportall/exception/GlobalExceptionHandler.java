// для исключений
package com.artyzh.doctorportall.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NotFoundException ex) {
        return body(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String, Object>> handleConflict(ConflictException ex) {
        return body(HttpStatus.CONFLICT, ex.getMessage());
    }

    // тюнинг под нагрузку: занятый слот теперь ловится на уникальном индексе БД при
    // конкурентной вставке; нарушение uq_appointments_doctor_slot -> 409, нарушение
    // FK на врача -> 404. Раньше это падало бы в 500 через RuntimeException-обработчик.
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleIntegrity(DataIntegrityViolationException ex) {
        String constraint = "";
        if (ex.getCause() instanceof ConstraintViolationException cve && cve.getConstraintName() != null) {
            constraint = cve.getConstraintName().toLowerCase();
        }
        if (constraint.contains("uq_appointments_doctor_slot")) {
            return body(HttpStatus.CONFLICT, "Time slot occupied");
        }
        if (constraint.contains("fk_appointment_doctor")) {
            return body(HttpStatus.NOT_FOUND, "Doctor not found");
        }
        return body(HttpStatus.BAD_REQUEST, "Data integrity violation");
    }

    // тюнинг под нагрузку: битый payload (невалидный JSON, неизвестный status) — это 400;
    // раньше HttpMessageNotReadableException как наследник RuntimeException давал 500
    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Map<String, Object>> handleBadRequest(Exception ex) {
        return body(HttpStatus.BAD_REQUEST, "Malformed request");
    }

    // запасной вариант для ещё не типизированных мест; выбор статуса по подстроке
    // сохранён только здесь, как совместимость
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String messageLower = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";

        if (messageLower.contains("not found")) {
            status = HttpStatus.NOT_FOUND;
        } else if (messageLower.contains("invalid") || messageLower.contains("bad request") || messageLower.contains("occupied")) {
            status = HttpStatus.BAD_REQUEST;
        }
        return body(status, ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> body(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}
