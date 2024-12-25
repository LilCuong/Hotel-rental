package com.n3fpoly.hotel_rental;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.n3fpoly.hotel_rental.common.AuthInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Đăng ký interceptor và áp dụng cho các đường dẫn bắt đầu bằng "/dashboard/"
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/dashboard/**") // Áp dụng cho tất cả các URL bắt đầu bằng "/dashboard/"
                .addPathPatterns("/client/**")
                .addPathPatterns("/auth/**");
    }
}
