//package com.universal.springbackend.config;
//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		http
//				.csrf().disable() // CSRF 보호 비활성화 (개발 중일 때만)
//				.authorizeHttpRequests(authorize -> authorize
//						.requestMatchers("/api/**").permitAll() // API 엔드포인트에 대한 접근 허용
//						.anyRequest().authenticated()
//				);
//		return http.build();
//	}
//}
