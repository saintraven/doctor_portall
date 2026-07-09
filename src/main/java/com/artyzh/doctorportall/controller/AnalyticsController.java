package com.artyzh.doctorportall.controller;

import com.artyzh.doctorportall.dto.AnalyticDto;
import com.artyzh.doctorportall.model.Doctor;
import com.artyzh.doctorportall.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/analytics")
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    // тюнинг под нагрузку: алиас /summary добавлен, не ломая существующий путь —
    // два из трёх шаблонов нагрузочных скриптов ожидают /api/v1/analytics/summary
    @GetMapping({"", "/summary"})
    public ResponseEntity<List<AnalyticDto>> getAll() {
        return ResponseEntity.ok(analyticsService.getAnalytics());
    }
}
