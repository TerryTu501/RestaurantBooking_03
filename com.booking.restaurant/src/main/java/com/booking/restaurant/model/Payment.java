package com.booking.restaurant.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "Payments")
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_id")
	private Integer paymentId;
	
	@Column(name = "reservation_id")
	private Integer reservationId;
	
	@Column(name = "amount")
	private BigDecimal amount;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "payment_method")
	private String paymentMethod;
	
	@Column(name = "transaction_id")
	private String transactionId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at")
	private Timestamp createdAt;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at")
	private Timestamp updatedAt;
//	Caused by: org.hibernate.MappingException: Column 'reservation_id' is duplicated in mapping for entity 'com.booking.restaurant.model.Payment' (use '@Column(insertable=false, updatable=false)' when mapping multiple properties to the same column)	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reservation_id", insertable = false, updatable = false)
	private Reservation reservation;
	
	@PrePersist
	protected void onCreate() {
		LocalDateTime now = LocalDateTime.now();
		createdAt = Timestamp.valueOf(now);
		updatedAt = Timestamp.valueOf(now);
	}

	@PreUpdate
	protected void onUpdate() {
		LocalDateTime now = LocalDateTime.now();
		updatedAt = Timestamp.valueOf(now);
	}

}
