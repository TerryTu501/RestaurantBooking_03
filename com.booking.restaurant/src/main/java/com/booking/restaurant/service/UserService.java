package com.booking.restaurant.service;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.booking.restaurant.dto.UserDTO;
import com.booking.restaurant.model.User;
import com.booking.restaurant.repository.UserRepository;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 註冊用戶
    public String registerUser(UserDTO userDTO) {
        // 檢查用戶名是否已存在
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            return "帳號已存在";
        }
        
        // 預設 UserType 為 restaurant_owner
        if (userDTO.getUserType() == null || userDTO.getUserType().isEmpty()) {
            userDTO.setUserType("restaurant_owner");
        }        
        
        // 建立新用戶
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPasswordHash(userDTO.getPasswordHash()); // 假設此密碼已加密
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setUserType(userDTO.getUserType());
        user.setCreatedAt(LocalDateTime.now());

        // 保存用戶到資料庫
        userRepository.save(user);
        return "註冊成功";
    }

    // 登入用戶
    public User loginUser(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // 驗證密碼
            if (user.getPasswordHash().equals(password)) {
                return user;
            }
        }
        return null;
    }

    // 根據用戶名查找用戶
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}