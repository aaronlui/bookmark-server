package com.lhboy.bookmark.config;

import com.lhboy.bookmark.common.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;
    public WebMvcConfig(JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/**")           // 拦截所有 API
                .excludePathPatterns(
                        "/api/auth/register",
                        "/api/auth/login",
                        "/doc.html",                  // Knife4j
                        "/webjars/**",
                        "/swagger-resources/**",
                        "/v2/**"
                );
    }
}
