package com.booking.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booking.restaurant.model.AdminLog;

public interface AdminLogRepository extends JpaRepository<AdminLog, Integer> {

}
