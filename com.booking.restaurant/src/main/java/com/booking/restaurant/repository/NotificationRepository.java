package com.booking.restaurant.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.booking.restaurant.model.Notification;


public interface NotificationRepository extends JpaRepository<Notification, Integer> {

	// 分頁功能(可根據"接收者和狀態查詢通知")
	Page<Notification> findByReceiverUserIdAndStatus(Integer receiverId, String status, Pageable pageable);

	// 根據通知 ID 列表查詢通知
    List<Notification> findByNotificationIdIn(List<Integer> ids);
	
    @Query("SELECT n FROM Notification n WHERE n.receiver.userId = :receiverId AND n.status = :status "
            + "AND n.createdAt BETWEEN :startDate AND :endDate")
    Page<Notification> findByReceiverAndStatusAndDateRange(
            @Param("receiverId") Integer receiverId,
            @Param("status") String status,
            @Param("startDate") Timestamp startDate,
            @Param("endDate") Timestamp endDate,
            Pageable pageable);
    
    
    
}
