package com.booking.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booking.restaurant.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

}
