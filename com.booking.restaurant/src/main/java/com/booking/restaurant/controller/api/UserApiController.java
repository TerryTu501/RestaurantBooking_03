package com.booking.restaurant.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.booking.restaurant.dto.UserDTO;
import com.booking.restaurant.model.User;
import com.booking.restaurant.service.UserService;
import com.booking.restaurant.validation.JsonWebTokenUtility;


@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class UserApiController {

	@Autowired
    private JsonWebTokenUtility jsonWebTokenUtility;

    @Autowired
    private UserService userService;

//  註冊功能
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        try {
        	// 先將 UserType 預設為 restaurant_owner
            if (userDTO.getUserType() == null || userDTO.getUserType().isEmpty()) {
                userDTO.setUserType("restaurant_owner");
            }
        	       	
            // 執行註冊邏輯
            String result = userService.registerUser(userDTO);

            if (result.equals("帳號已存在")) {
                return ResponseEntity.status(409).body("帳號已存在");
            }

            // 註冊成功，取出用戶資料
            User user = userService.getUserByUsername(userDTO.getUsername());
            if (user == null) {
                return ResponseEntity.status(500).body("用戶註冊成功，但無法獲取用戶資料");
            }

            // 生成 JWT Token
            JSONObject userJson = new JSONObject()
                    .put("userId", user.getUserId())
                    .put("username", user.getUsername());
            String token = jsonWebTokenUtility.createToken(userJson.toString());

            // 返回用戶資料和 Token
            JSONObject responseJson = new JSONObject()
                    .put("success", true)
                    .put("message", "註冊成功")
                    .put("token", token)
                    .put("user", new JSONObject()
                            .put("userId", user.getUserId())
                            .put("username", user.getUsername()));

            return ResponseEntity.ok(responseJson.toString());
        } catch (JSONException e) {
            return ResponseEntity.status(500).body("JSON 格式化錯誤：" + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDTO userDTO) {
        try {
            // 驗證帳號和密碼
            User user = userService.loginUser(userDTO.getUsername(), userDTO.getPasswordHash());
            if (user == null) {
                return ResponseEntity.status(401).body("帳號或密碼錯誤");
            }

            // 生成簡化的 JWT Token
            JSONObject userJson = new JSONObject()
                    .put("userId", user.getUserId())
                    .put("username", user.getUsername());
            String token = jsonWebTokenUtility.createToken(userJson.toString());

            // 回傳 Token 與必要用戶資料
            JSONObject responseJson = new JSONObject()
                    .put("success", true)
                    .put("message", "登入成功")
                    .put("token", token)
                    .put("user", new JSONObject()
                            .put("userId", user.getUserId())
                            .put("username", user.getUsername()));

            return ResponseEntity.ok(responseJson.toString());
        } catch (JSONException e) {
            return ResponseEntity.status(500).body("JSON 格式化錯誤：" + e.getMessage());
        }
    }

}