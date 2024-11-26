package com.booking.restaurant.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class JwtConfig implements WebMvcConfigurer {
    @Autowired
    private JsonWebTokenInterceptor jsonWebTokenInterceptor;

    @SuppressWarnings("null")
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jsonWebTokenInterceptor)
            .addPathPatterns("/ajax/pages/**")
            .addPathPatterns("/rest/pages/**");
    }
}