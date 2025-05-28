package com.CloudWhite.PersonalBlog.Model.Config;

import com.CloudWhite.PersonalBlog.Utils.Interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new Interceptor(stringRedisTemplate)).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

//    @Override
//    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
//        super.addResourceHandlers(registry);
//    }
}
