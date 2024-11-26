package com.booking.restaurant.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.booking.restaurant.dto.NotificationDTO;
import com.booking.restaurant.model.Notification;
import com.booking.restaurant.model.User;
import com.booking.restaurant.repository.NotificationRepository;
import com.booking.restaurant.repository.UserRepository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    // 新增通知
    public NotificationDTO createNotification(NotificationDTO notificationDTO) {
        Notification notification = convertToEntity(notificationDTO);
        Notification savedNotification = notificationRepository.save(notification);
        return convertToDTO(savedNotification);
    }

    // 查詢通知 (升降冪排序+"日期、ID、讀取狀態"排序)
    public Page<NotificationDTO> getNotifications(Integer userId, String status, Timestamp startDate, 
    		Timestamp endDate, String sortDirection, int page, int size) {    	
    	
    	Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "createdAt");
        Page<Notification> notifications = notificationRepository.findByReceiverAndStatusAndDateRange(
                userId, status, startDate, endDate, PageRequest.of(page, size, sort));
        return notifications.map(this::convertToDTO);
    }

    // 標記通知為已讀
    public List<NotificationDTO> markNotificationsAsRead(List<Integer> notificationIds) {
        // 查詢需要更新的通知
        List<Notification> notifications = notificationRepository.findByNotificationIdIn(notificationIds);

        // 更新狀態為已讀
        notifications.forEach(notification -> notification.setStatus("Read"));
        notificationRepository.saveAll(notifications);

        // 將更新後的通知轉換為 DTO 並返回
        return notifications.stream()
                .map(this::convertToDTO)
                .toList();
    }

    // DTO to Entity
    private Notification convertToEntity(NotificationDTO dto) {
        Notification notification = new Notification();
        notification.setNotificationId(dto.getNotificationId());
        notification.setContent(dto.getContent());
        notification.setStatus(dto.getStatus());
        notification.setNotificationType(dto.getNotificationType());
        notification.setCreatedAt(dto.getCreatedAt());

        // 查詢發送者並設置
        if (dto.getSenderUsername() != null) {
            User sender = userRepository.findByUsername(dto.getSenderUsername())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid sender username: " + dto.getSenderUsername()));
            notification.setSender(sender);
        }

        // 查詢接收者並設置
        if (dto.getReceiverUsername() != null) {
            User receiver = userRepository.findByUsername(dto.getReceiverUsername())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid receiver username: " + dto.getReceiverUsername()));
            notification.setReceiver(receiver);
        }

        return notification;
    }

    // Entity to DTO
    private NotificationDTO convertToDTO(Notification notification) {
        String senderUsername = notification.getSender() != null ? notification.getSender().getUsername() : null;
        String receiverUsername = notification.getReceiver() != null ? notification.getReceiver().getUsername() : null;

        return new NotificationDTO(
                notification.getNotificationId(),
                notification.getContent(),
                notification.getStatus(),
                notification.getCreatedAt(),
                notification.getNotificationType(),
                senderUsername,
                receiverUsername
        );
    }
}
