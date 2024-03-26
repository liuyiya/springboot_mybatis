package com.evolution.config.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 允许跨域请求携带cookie等信息
        config.addAllowedOriginPattern("*"); // 允许任何域名使用（使用 addAllowedOriginPattern 方法代替 addAllowedOrigin 方法）
        config.addAllowedHeader("*"); // 允许任何头信息
        config.addAllowedMethod("*"); // 允许任何请求方法（使用 addAllowedMethod("*") 方法代替 addAllowedMethod("GET", "POST", "PUT", "DELETE", "OPTIONS")）
        source.registerCorsConfiguration("/**", config); // 对指定路径进行跨域配置
        return new CorsFilter(source);
    }
}

