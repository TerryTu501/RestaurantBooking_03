package com.booking.restaurant.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // 映射根路徑 "/" 到登入頁面
	 @GetMapping("/")
	    public String home() {
	        return "redirect:/user/loginPage"; // 確保重定向到 "/user/loginPage"
	    }
}

