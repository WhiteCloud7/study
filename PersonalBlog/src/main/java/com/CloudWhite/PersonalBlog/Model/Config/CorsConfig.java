package com.CloudWhite.PersonalBlog.Model.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        // 创建 CORS 配置对象
        CorsConfiguration config = new CorsConfiguration();
        // 允许的跨域请求源，可以使用具体的域名，也可以使用通配符 "*" 允许所有源
        // 注意：在生产环境中，建议使用具体的域名，避免使用通配符，以增强安全性
        config.addAllowedOriginPattern("*");
        // 允许的请求方法，如 GET、POST、PUT、DELETE 等
        config.addAllowedMethod("*");
        // 允许的请求头，如 Content-Type、Authorization 等
        config.addAllowedHeader("*");
        // 允许浏览器获取的响应头
        config.addExposedHeader("*");
        // 是否允许携带凭证，如 Cookie、HTTP 认证信息等
        config.setAllowCredentials(true);
        // 预检请求的缓存时间（秒），在此时间内，相同的预检请求将不再发送
        config.setMaxAge(3600L);
        // 创建基于 URL 的 CORS 配置源
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 对所有接口都应用 CORS 配置
        source.registerCorsConfiguration("/**", config);
        // 创建并返回 CORS 过滤器
        return new CorsFilter(source);
    }
}