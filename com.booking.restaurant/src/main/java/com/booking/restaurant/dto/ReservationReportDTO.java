package com.booking.restaurant.dto;

import java.util.List;

public class ReservationReportDTO {
    private Integer year;              // 年份
    private Integer month;             // 月份（可選，用於日報表）
    private Integer restaurantId;      // 餐廳ID
    private List<Integer> counts;      // 每日或每月的訂位數量

    // Constructors
    public ReservationReportDTO() {}

    // 用於生成按月或按日報表
    public ReservationReportDTO(Integer year, Integer month, Integer restaurantId, List<Integer> counts) {
        this.year = year;
        this.month = month;
        this.restaurantId = restaurantId;
        this.counts = counts;
    }

    // 用於特定日期的報表數據（單筆數據）
    public ReservationReportDTO(Integer year, Integer month, Integer restaurantId, Integer count) {
        this.year = year;
        this.month = month;
        this.restaurantId = restaurantId;
        this.counts = List.of(count);
    }

    // Getters and Setters
    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public List<Integer> getCounts() {
        return counts;
    }

    public void setCounts(List<Integer> counts) {
        this.counts = counts;
    }
}

