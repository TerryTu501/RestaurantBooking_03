package com.booking.restaurant.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.booking.restaurant.model.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    Page<Reservation> findByReservationTimeBetween(LocalDateTime start, LocalDateTime end, PageRequest pageable);

    Page<Reservation> findByReservationTimeBetweenAndStatus(LocalDateTime start, LocalDateTime end, 
    						Reservation.ReservationStatus status, Pageable pageable);
    
//-----報表相關
    
//  查詢每月訂位數量
    @Query("SELECT MONTH(r.reservationTime) AS month, COUNT(r) AS count " +
           "FROM Reservation r " +
           "WHERE YEAR(r.reservationTime) = :year " +
           "AND (:restaurantId IS NULL OR r.restaurant.restaurantId = :restaurantId) " +
           "GROUP BY MONTH(r.reservationTime)")
    List<Object[]> findReservationCountByYearAndRestaurant(Integer year, Integer restaurantId);

//  查詢每天訂位數量
    @Query("SELECT DAY(r.reservationTime) AS day, COUNT(r) AS count " +
           "FROM Reservation r " +
           "WHERE YEAR(r.reservationTime) = :year AND MONTH(r.reservationTime) = :month " +
           "AND (:restaurantId IS NULL OR r.restaurant.restaurantId = :restaurantId) " +
           "GROUP BY DAY(r.reservationTime)")
    List<Object[]> findReservationCountByDayForRestaurant(Integer year, Integer month, Integer restaurantId);

//  查詢指定日期範圍內的訂位數據
    @Query("SELECT r.reservationTime, COUNT(r) AS count " +
	       "FROM Reservation r " +
	       "WHERE r.reservationTime BETWEEN :startDateTime AND :endDateTime " +
	       "AND (:restaurantId IS NULL OR r.restaurant.restaurantId = :restaurantId) " +
	       "GROUP BY r.reservationTime")
	List<Object[]> findReservationDataBetweenDates(LocalDateTime startDateTime, LocalDateTime endDateTime, Integer restaurantId);
    
}
