package com.booking.restaurant.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "admin_logs")
public class AdminLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id", nullable = false)
    private Integer logId;

    @ManyToOne
    @JoinColumn(name = "admin_id", insertable = false, updatable = false)
    private User admin;  // 假設 Admin 是 User 表的一部分
    
    @Column(name = "admin_id", nullable = false)
    private Integer adminId;

    @Column(name = "action", nullable = false, length = 100)
    private String action;

    @Column(name = "details", columnDefinition = "nvarchar(MAX)")
    private String details;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

	@Override
	public String toString() {
		return "AdminLog [logId=" + logId + ", adminId=" + adminId + ", action=" + action + ", details=" + details
				+ ", createdAt=" + createdAt + "]";
	}
    
}
