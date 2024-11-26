package com.booking.restaurant.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.booking.restaurant.dto.ApiResponse;
import com.booking.restaurant.dto.ReservationDTO;
import com.booking.restaurant.model.Reservation;
import com.booking.restaurant.service.ReservationService;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin
public class ReservationApiController {

    @Autowired
    private ReservationService reservationService;
    
//  取得訂位資料
    @GetMapping
    public ResponseEntity<ApiResponse> getReservations(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Reservation.ReservationStatus status,
            @RequestParam(defaultValue = "DESC") String sortDirection, //預設降冪條件
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

    	List<ReservationDTO> reservations = reservationService.getReservations(date, startDate, endDate, status, sortDirection, page, size);
    	ApiResponse response = new ApiResponse("success", "Query executed successfully", reservations);
    	return ResponseEntity.ok(response);
    }

//  取消訂位(後發通知給客戶)
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<ApiResponse> cancelReservation(
            @PathVariable Integer reservationId,
            @RequestParam(required = false) String reason,
            @RequestParam(defaultValue = "0") Integer refundAmount) {
        try {
            // 調用服務層的取消邏輯
            String message = reservationService.cancelReservation(reservationId, reason, refundAmount);
            // 返回成功響應
            ApiResponse response = new ApiResponse("success", message);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException | IllegalStateException e) {
            // 捕獲業務邏輯異常並返回錯誤響應
            ApiResponse response = new ApiResponse("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
  
    
    
    
    
    
    
}

