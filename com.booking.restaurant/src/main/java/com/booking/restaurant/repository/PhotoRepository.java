package com.booking.restaurant.repository;

import com.booking.restaurant.model.Restaurant;
import com.booking.restaurant.model.RestaurantPhotos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<RestaurantPhotos, Integer> {
    
    // 查詢餐廳的所有照片
    @Query(value = """
        SELECT * FROM dbo.RestaurantPhotos 
        WHERE restaurant_id = :restaurantId 
        ORDER BY display_order ASC
        """, nativeQuery = true)
    List<RestaurantPhotos> findByRestaurantId(@Param("restaurantId") Integer restaurantId);
        
    // 查詢特定類型的照片
    @Query(value = """
        SELECT * FROM dbo.RestaurantPhotos 
        WHERE restaurant_id = :restaurantId 
        AND photo_type = :photoType 
        ORDER BY display_order ASC
        """, nativeQuery = true)
    List<RestaurantPhotos> findByRestaurantIdAndPhotoType(
        @Param("restaurantId") Integer restaurantId,
        @Param("photoType") String photoType
    );
    
    // 獲取最大顯示順序
    @Query(value = """
        SELECT MAX(display_order) 
        FROM dbo.RestaurantPhotos 
        WHERE restaurant_id = :restaurantId
        """, nativeQuery = true)
    Integer findMaxDisplayOrder(@Param("restaurantId") Integer restaurantId);
    
    /**
     * 查找指定餐廳的封面照片（PhotoType 為 cover，並且 IsActive 為 true）
     */
    RestaurantPhotos findFirstByRestaurantAndPhotoTypeAndIsActive(Restaurant restaurant, String photoType, boolean isActive);
    
}