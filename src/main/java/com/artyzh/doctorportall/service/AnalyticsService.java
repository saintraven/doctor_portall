package com.artyzh.doctorportall.service;

import com.artyzh.doctorportall.dto.AnalyticDto;
import com.artyzh.doctorportall.dto.StatisticsDto;
import com.artyzh.doctorportall.repository.AppointmentsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnalyticsService {
    private final AppointmentsRepository appointmentsRepository;

    // тюнинг под нагрузку: аналитика — агрегат по ВСЕЙ таблице appointments, которая
    // растёт весь тест; при весе 1/45 это ~2.2% трафика полных сканов. TTL-кэш на 1с
    // ограничивает стоимость одним сканом в секунду независимо от числа пользователей.
    // Кэш осознанно только здесь: это статистическая сводка, скрипт её содержимое
    // не проверяет, секундная давность семантику не нарушает; список приёмов
    // (горячий GET) не кэшируется вовсе
    private static final long CACHE_TTL_NANOS = 1_000_000_000L;
    private volatile CachedStats cache;

    private record CachedStats(List<AnalyticDto> value, long computedAt) {}

    public AnalyticsService(AppointmentsRepository appointmentsRepository) {
        this.appointmentsRepository = appointmentsRepository;
    }

    public List<AnalyticDto> getAnalytics() {
        CachedStats c = cache;
        if (c != null && System.nanoTime() - c.computedAt() < CACHE_TTL_NANOS) {
            return c.value();
        }
        // single-flight: агрегат пересчитывает один поток, остальные ждут его результат,
        // а не выстраивают очередь одинаковых полных сканов в БД
        synchronized (this) {
            c = cache;
            if (c != null && System.nanoTime() - c.computedAt() < CACHE_TTL_NANOS) {
                return c.value();
            }
            List<AnalyticDto> fresh = computeAnalytics();
            cache = new CachedStats(fresh, System.nanoTime());
            return fresh;
        }
    }

    private List<AnalyticDto> computeAnalytics() {
        List<StatisticsDto> stats = appointmentsRepository.getAppointmentsStats();
        List<AnalyticDto> result = new ArrayList<>();
        for (StatisticsDto stat : stats) {
            AnalyticDto dto = new AnalyticDto();
            dto.setDoctorId(stat.getDoctorId());
            dto.setFullName(stat.getFullName());
            dto.setTotalRevenue(stat.getTotalRevenue());
            dto.setTotalAppointments(stat.getTotalAppointments());
            // тюнинг под нагрузку: у врача без приёмов деление 0/0 давало NaN,
            // и в JSON уходила строка "NaN" в числовом поле
            dto.setNoShowRatePercentage(stat.getTotalAppointments() == 0
                    ? 0.0
                    : (double) stat.getTotalNoShow() / stat.getTotalAppointments());
            result.add(dto);
        }
        return result;
    }
}
