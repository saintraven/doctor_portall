// для исключений
package com.artyzh.doctorportall.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler(RuntimeException.class)
    public void handleRuntimeException(RuntimeException ex, HttpServletResponse response) throws IOException {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String messageLower = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";

        if (messageLower.contains("not found")) {
            status = HttpStatus.NOT_FOUND;
        } else if (messageLower.contains("invalid") || messageLower.contains("bad request") || messageLower.contains("occupied")) {
            status = HttpStatus.BAD_REQUEST;
        }

        response.setStatus(status.value());
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", ex.getMessage());

        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
