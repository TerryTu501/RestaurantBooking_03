package com.booking.restaurant.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JsonWebTokenInterceptor implements HandlerInterceptor {
    @Autowired
    private JsonWebTokenUtility jsonWebTokenUtility;

    @SuppressWarnings("null")
	@Override
    public boolean preHandle(HttpServletRequest request,
                        HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        if("OPTIONS".equals(method)) {
            return true;
        }
        String auth = request.getHeader("Authorization");
        if(auth!=null && auth.startsWith("Bearer ")) {
            auth = auth.substring(7);
            String subject = jsonWebTokenUtility.validateToken(auth);
            if(subject!=null && subject.length()!=0) {
                //驗證TOken成功：true
                return true;
            }
        }
        //驗證TOken失敗：false
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");
        return false;
    }
    
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:5173") // 前端的域名
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .allowedHeaders("*")
//                .allowCredentials(true);
//    }
    
}

