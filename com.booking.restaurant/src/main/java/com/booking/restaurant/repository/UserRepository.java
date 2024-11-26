package com.booking.restaurant.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.booking.restaurant.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
   
	// 根據用戶名查找用戶
	Optional<User> findByUsername(String username);
    
	// 判斷用戶名是否存在
	boolean existsByUsername(String username);
    

}
