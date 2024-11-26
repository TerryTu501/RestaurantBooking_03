package com.booking.restaurant.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "Notifications")
public class Notification {
	
	public enum NotificationType {
	    RESERVATION_NOTIFICATION,  // 訂位通知
	    REVIEW_REMINDER,           // 評分/評論通知
	    SYSTEM_NOTIFICATION        // 系統通知（包括審核結果等）
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notification_id")
	private Integer notificationId;
	
	//暫不使用，用ID去判斷為何類型
//	@Column(name = "sender_type")
//	private String senderType; // 發送者的類型( 使用者或老闆 )
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnore
	private User sender; // 發送者的ID
	
	@Column(name = "notification_type", nullable = false)
	@Enumerated(EnumType.STRING) // 將枚舉值存為字串
	private NotificationType notificationType; // 通知類型	

	@Column(name = "content")
	private String content; // 通知的具體內容

	@Column(name = "status")
	private String status; // Unread、Read、Replied
	
	//狀態暫時不改為以下
//	@Column(name = "is_read", nullable = false)
//	private Boolean isRead = false; // 是否已讀，默認為未讀
		
	@Column(name = "created_at")
	private Timestamp createdAt;
			
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver_id", nullable = false)
	@JsonIgnore
	private User receiver; // 接收者的ID
	
	// 與 Review 的關聯（例如用於評論通知）
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "review_id", nullable = true)
//	@JsonIgnore
//	private Review review; // 可選的關聯評論
	
		
//  不用admin，改為任一USER	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "admin_id", nullable = false)
//	@JsonIgnore
//	private Admin admin; // 接收者的ID

	@PrePersist
	protected void onCreate() {
		LocalDateTime now = LocalDateTime.now();
		 if (status == null || status.isEmpty()) {
	            status = "Unread"; // 確保狀態默認為未讀
	        }
		createdAt = Timestamp.valueOf(now);
	}
	
	//Getter&Setter
	public void markAsRead() {
        this.status = "Read"; // 標記為已讀
    }

	public Integer getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Integer notificationId) {
		this.notificationId = notificationId;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public NotificationType getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(NotificationType notificationType) {
		this.notificationType = notificationType;
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

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}
	



}
