package com.example.hotel_booking_service.web.controller;

import com.example.hotel_booking_service.aop.LogExecution;
import com.example.hotel_booking_service.statistics.export.StatisticExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticController {
    private final StatisticExportService exportService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @LogExecution
    public ResponseEntity<byte[]> export() {
        byte[] csv = exportService.exportCsv();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=statistics.csv")
                .body(csv);
    }
}
