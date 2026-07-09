package com.artyzh.doctorportall.exception;

// тюнинг под нагрузку: типизированное 404 вместо выбора статуса подстрокой
// в сообщении RuntimeException — под нагрузкой любое "чужое" исключение
// с иным текстом превращалось в 500
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
