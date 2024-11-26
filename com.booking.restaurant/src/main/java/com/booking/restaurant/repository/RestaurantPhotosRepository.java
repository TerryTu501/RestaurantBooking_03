package com.booking.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booking.restaurant.model.RestaurantPhotos;

public interface RestaurantPhotosRepository extends JpaRepository<RestaurantPhotos, Integer> {

}
