package com.booking.restaurant.config;

//import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration	
public class SecurityConfig {

//	密碼(加密)功能
//	//於pom.xml加入函示庫org.springframework.security/spring-security-crypto
//	@Bean 
//	PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
	
//	設置訪問限制以確保 index 頁面只能在用戶登入後訪問，您可以按照以下步驟來配置 Spring Security
//	 @Bean
//	    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//	        http
//	            .authorizeHttpRequests(auth -> {
//	                auth
//	                    .requestMatchers("/", "/users/registerPage", "/users/registerPost").permitAll() // 設置這些路徑為公開訪問
//	                    .requestMatchers("/index").authenticated() // 設置 "/index" 只能在用戶登入後訪問
//	                    .anyRequest().authenticated(); // 其他路徑需要身份驗證
//	            })
//	            .formLogin(form -> form
//	                .loginPage("/") // 設置自定義登入頁面
//	                .defaultSuccessUrl("/index", true) // 登入成功後跳轉到 "/index"
//	                .permitAll()
//	            )
//	            .logout(logout -> logout
//	                .logoutUrl("/logout")
//	                .logoutSuccessUrl("/")
//	                .permitAll()
//	            )
//	            .csrf(csrf -> csrf.disable()); // 根據需求關閉 CSRF 保護
//
//	        return http.build();
//	    }
}
