package com.booking.restaurant.dto;

import java.sql.Timestamp;

import com.booking.restaurant.model.Notification.NotificationType;

public class NotificationDTO {

    private Integer notificationId;
    private String content;
    private String status;
    private Timestamp createdAt;
    private NotificationType notificationType;   
    
    // 僅包含必要的發送者和接收者名稱以避免遞迴
    private String senderUsername; // 發送者名稱
    private String receiverUsername; // 接收者名稱

    // Constructor
    public NotificationDTO(Integer notificationId, String content, String status, Timestamp createdAt,
                           NotificationType notificationType, String senderUsername, String receiverUsername) {
        this.notificationId = notificationId;
        this.content = content;
        this.status = status;
        this.createdAt = createdAt;
        this.notificationType = notificationType;
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
    }

    // Getters and Setters
    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }
}
