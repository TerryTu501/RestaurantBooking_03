package com.booking.restaurant.service;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.booking.restaurant.dto.ReservationDTO;
import com.booking.restaurant.dto.ReservationReportDTO;
import com.booking.restaurant.model.Reservation;
import com.booking.restaurant.model.RestaurantTable;
import com.booking.restaurant.repository.ReservationRepository;
import com.booking.restaurant.repository.RestaurantTableRepository;

import jakarta.transaction.Transactional;

    @Service
    public class ReservationService {

    	@Autowired
        private ReservationRepository reservationRepository;
    	
    	@Autowired
		private RestaurantTableRepository restaurantTableRepository;

    	@Autowired
    	private EmailService emailService;
    	
//		取得訂位資料-支持日期範圍、狀態篩選 & 升降冪排序功能 
        @SuppressWarnings("null")
		public List<ReservationDTO> getReservations(String date, String startDate, String endDate, 
                Reservation.ReservationStatus status, String sortDirection, int page, int size) {
			// 創建 Sort 對象
			Sort sort = Sort.by(Sort.Direction.fromString(sortDirection.toUpperCase()), "reservationTime");
			
			// 創建 Pageable 對象，包含分頁和排序
			PageRequest pageable = PageRequest.of(page, size, sort);
			
			Page<Reservation> reservationPage;
			
			try {
				// 日期解析
				LocalDateTime start = (startDate != null) ? LocalDate.parse(startDate).atStartOfDay() : null;
				LocalDateTime end = (endDate != null) ? LocalDate.parse(endDate).atTime(LocalTime.MAX) : null;
				
				// 確保有默認查詢範圍
				if (start == null && end == null) {
					LocalDateTime now = LocalDateTime.now();
					end = now;
					start = now.minusDays(7); // 默認查詢最近 7 天
				} else if (start == null) {
					start = end.minusDays(7); // 若僅提供結束日期，則查詢結束日期前 7 天
				} else if (end == null) {
					end = start.plusDays(7); // 若僅提供開始日期，則查詢開始日期後 7 天
				}
				
				// 查詢邏輯
				if (status != null) {
					// 查詢帶狀態的記錄
					reservationPage = reservationRepository.findByReservationTimeBetweenAndStatus(start, end, status, pageable);
				} else {
					// 查詢不帶狀態的記錄
					reservationPage = reservationRepository.findByReservationTimeBetween(start, end, pageable);
				}
				
				} catch (DateTimeParseException e) {
					throw new IllegalArgumentException("日期格式錯誤，請使用 'yyyy-MM-dd' 格式", e);
				} catch (IllegalArgumentException e) {
					throw new IllegalArgumentException("狀態格式錯誤，請使用有效的狀態值（PENDING, CONFIRMED, CANCELLED, COMPLETED）", e);
				}
				
				// 將結果轉換為 DTO 並返回
				return reservationPage.getContent().stream()
					.map(this::convertToDTO)
					.collect(Collectors.toList());
			}
			
		// DTO 轉換邏輯
		private ReservationDTO convertToDTO(Reservation reservation) {
				return new ReservationDTO(
					reservation.getReservationId(),
					reservation.getPartySize(),
					reservation.getReservationTime(),
					reservation.getStatus().name(),
					reservation.getUser().getUsername()
				);
			}		
//		END: 取得訂位資料
			
			
//		取消訂位-取消後，發送通知給客戶並更新桌位狀態為 MAINTENANCE & 條件:只有訂單狀態為"CONFIRMED"才能被取消 
		@Transactional
	    public String cancelReservation(Integer reservationId, String reason, Integer refundAmount) {
	        // 根據 ID 查詢訂位
	        Reservation reservation = reservationRepository.findById(reservationId)
	                .orElseThrow(() -> new IllegalArgumentException("找不到該訂位，無法取消"));

	        // 確保狀態為 "CONFIRMED"，否則拋出異常
	        if (!Reservation.ReservationStatus.CONFIRMED.equals(reservation.getStatus())) {
	            throw new IllegalStateException("只有狀態為 'CONFIRMED' 的訂單可以取消");
	        }

	        // 更新訂位狀態為 "CANCELLED"
	        reservation.setStatus(Reservation.ReservationStatus.CANCELLED);
	        reservation.setUpdatedAt(LocalDateTime.now());

	        // 更新桌位狀態為 MAINTENANCE
	        if (reservation.getTable() != null) {
	            RestaurantTable table = reservation.getTable();
	            table.setStatus(RestaurantTable.TableStatus.MAINTENANCE);
	            restaurantTableRepository.save(table);
	        }

	        // 在取消原因中記錄附加資訊
	        if (reason != null && !reason.isBlank()) {
	            reservation.setSpecialRequests("取消原因: " + reason);
	        }

	        // 發送通知給客戶（模擬功能）
	        sendNotificationToCustomer(reservation, reason, refundAmount);

	        // 返回訊息
	        return String.format("訂位已取消，原因：%s，退款金額為 %d 元", reason, refundAmount);
	    }

	    // 模擬通知功能
		private void sendNotificationToCustomer(Reservation reservation, String reason, double refundAmount) {
		    String customerEmail = reservation.getUser().getEmail();
		    String customerName = reservation.getUser().getUsername();
		    String reservationTime = reservation.getReservationTime().toString();
		    String tableNumber = reservation.getTable() != null ? reservation.getTable().getTableNumber() : "未指定";

		    // 構造郵件標題與內容
		    String subject = "【餐廳通知】您的訂位已取消";
		    String body = String.format(
		        "親愛的 %s，\n\n" +
		        "感謝您選擇我們的餐廳，以下是您的訂位取消通知：\n\n" +
		        "訂位資訊：\n" +
		        "- 訂位日期與時間：%s\n" +
		        "- 桌位號碼：%s\n\n" +
		        "取消原因：%s\n\n" +
		        "退款金額：%.2f 元\n\n" +
		        "我們為給您帶來的不便深表歉意。如有任何疑問，請隨時聯繫我們的客服團隊。\n\n" +
		        "敬祝順安，\n" +
		        "餐廳管理團隊",
		        customerName,
		        reservationTime,
		        tableNumber,
		        reason != null && !reason.isBlank() ? reason : "無",
		        refundAmount
		    );

		    // 發送郵件
		    emailService.sendEmail(customerEmail, subject, body);

		    // 記錄郵件發送操作
		    System.out.printf("通知已成功發送至用戶的電子郵件：%s\n主題：%s\n內容：\n%s\n", customerEmail, subject, body);
		}
//		END: 取消訂位					
    
			   
//--------以下為報表相關
		// 月報表
	    public ReservationReportDTO getMonthlyReservationReport(Integer year, Integer restaurantId) {
	        if (year == null) year = LocalDate.now().getYear();

	        List<Object[]> results = reservationRepository.findReservationCountByYearAndRestaurant(year, restaurantId);
	        List<Integer> monthlyCounts = new ArrayList<>(Collections.nCopies(12, 0)); // 初始化每月數據為 0

	        for (Object[] result : results) {
	            int month = (int) result[0] - 1; // 月份從 1 開始，需要轉為 0 基數
	            monthlyCounts.set(month, ((Number) result[1]).intValue());
	        }

	        return new ReservationReportDTO(year, null, restaurantId, monthlyCounts);
	    }

	    // 日報表
	    public ReservationReportDTO getDailyReservationReport(Integer year, Integer month, Integer restaurantId) {
	        if (year == null) year = LocalDate.now().getYear();
	        if (month == null) month = LocalDate.now().getMonthValue();

	        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
	        List<Object[]> results = reservationRepository.findReservationCountByDayForRestaurant(year, month, restaurantId);
	        List<Integer> dailyCounts = new ArrayList<>(Collections.nCopies(daysInMonth, 0)); // 初始化每日數據為 0

	        for (Object[] result : results) {
	            int day = (int) result[0] - 1; // 日期從 1 開始，需要轉為 0 基數
	            dailyCounts.set(day, ((Number) result[1]).intValue());
	        }

	        return new ReservationReportDTO(year, month, restaurantId, dailyCounts);
	    }

	    // 導出報表 (CSV 格式)
	    public byte[] exportReportToCsv(LocalDate startDate, LocalDate endDate, Integer restaurantId) {
	        // 將 LocalDate 轉為 LocalDateTime（起始日 00:00，結束日 23:59:59）
	        LocalDateTime startDateTime = startDate.atStartOfDay();
	        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

	        List<Object[]> results = reservationRepository.findReservationDataBetweenDates(startDateTime, endDateTime, restaurantId);

	        if (results == null || results.isEmpty()) {
	            return null; // 返回空結果，由 Controller 處理
	        }

	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        PrintWriter writer = new PrintWriter(outputStream);

	        writer.println("Date,Reservation Count");
	        for (Object[] result : results) {
	            writer.printf("%s,%d%n", result[0].toString(), ((Number) result[1]).intValue());
	        }
	        writer.flush();

	        return outputStream.toByteArray();
	    }
       
    }
