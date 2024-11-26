package com.booking.restaurant.dto;

import java.time.LocalDateTime;

public class ReservationDTO {
    private Integer reservationId;
    private Integer partySize;
    private LocalDateTime reservationTime;
    private String status;
    private String customerName;

    public ReservationDTO(Integer reservationId, Integer partySize, LocalDateTime reservationTime, String status, String customerName) {
        this.reservationId = reservationId;
        this.partySize = partySize;
        this.reservationTime = reservationTime;
        this.status = status;
        this.customerName = customerName;
    }

	public Integer getReservationId() {
		return reservationId;
	}

	public void setReservationId(Integer reservationId) {
		this.reservationId = reservationId;
	}

	public Integer getPartySize() {
		return partySize;
	}

	public void setPartySize(Integer partySize) {
		this.partySize = partySize;
	}

	public LocalDateTime getReservationTime() {
		return reservationTime;
	}

	public void setReservationTime(LocalDateTime reservationTime) {
		this.reservationTime = reservationTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

    // Getters and Setters
    
}
