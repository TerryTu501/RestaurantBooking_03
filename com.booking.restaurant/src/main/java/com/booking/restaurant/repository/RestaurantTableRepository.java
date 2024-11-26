package com.booking.restaurant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booking.restaurant.model.RestaurantTable;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Integer> {

	List<RestaurantTable> findByRestaurantRestaurantId(Integer restaurantId);
	
}
