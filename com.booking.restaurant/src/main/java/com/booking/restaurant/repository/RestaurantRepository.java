package com.booking.restaurant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.booking.restaurant.model.Restaurant;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant,  Integer> {

	List<Restaurant> findByOwner_UserId(Integer ownerId);
	
	
	
}