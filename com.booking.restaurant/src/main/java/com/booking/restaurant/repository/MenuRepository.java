package com.booking.restaurant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.booking.restaurant.model.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    
//  可以根據餐廳 ID 查詢菜單
    List<Menu> findByRestaurantRestaurantId(Integer restaurantId);
        
//  檢查菜單是否存在於指定的餐廳中
    boolean existsByMenuIdAndRestaurantRestaurantId(Integer menuId, Integer restaurantId);
    
    
}

