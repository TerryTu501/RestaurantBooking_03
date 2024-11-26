package com.booking.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booking.restaurant.model.MenuItem;


public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {

//	驗證指定的菜品是否屬於某個菜單及其餐廳
	boolean existsByItemIdAndMenuMenuIdAndMenuRestaurantRestaurantId(Integer itemId, Integer menuId, Integer restaurantId);	
}
