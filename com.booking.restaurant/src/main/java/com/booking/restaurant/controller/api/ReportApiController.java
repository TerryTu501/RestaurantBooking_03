package com.booking.restaurant.controller.api;

import com.booking.restaurant.dto.ReservationReportDTO;
import com.booking.restaurant.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@RestController
@CrossOrigin
@RequestMapping("/api/reports")
public class ReportApiController {

    @Autowired
    private ReservationService reservationService;

 // 月報表 API
    @GetMapping("/monthly")
    public ResponseEntity<ReservationReportDTO> getMonthlyReservationReport(
        @RequestParam(required = false) Integer year,
        @RequestParam(required = false) Integer restaurantId
    ) {
        try {
            ReservationReportDTO report = reservationService.getMonthlyReservationReport(year, restaurantId);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null); // 返回空物件作為錯誤回應
        }
    }

    // 日報表 API
    @GetMapping("/daily")
    public ResponseEntity<ReservationReportDTO> getDailyReservationReport(
        @RequestParam(required = false) Integer year,
        @RequestParam(required = false) Integer month,
        @RequestParam(required = false) Integer restaurantId
    ) {
        try {
            ReservationReportDTO report = reservationService.getDailyReservationReport(year, month, restaurantId);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null); // 返回空物件作為錯誤回應
        }
    }

    // 導出報表 (CSV 格式)
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportReportToCsv(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @RequestParam(required = false) Integer restaurantId
    ) {
        try {
            byte[] csvData = reservationService.exportReportToCsv(startDate, endDate, restaurantId);

            if (csvData == null || csvData.length == 0) {
                return ResponseEntity.badRequest()
                    .body("No data available for the specified date range.".getBytes());
            }

            return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=report.csv")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(csvData);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(("Error generating report: " + e.getMessage()).getBytes());
        }
    }
}

