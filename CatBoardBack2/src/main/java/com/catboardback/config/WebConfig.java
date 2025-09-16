package com.catboardback.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Spring Boot 설정 예시
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${boardImgLocation}")
    private String boardImgLocation;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /images/** 요청 → boardImgLocation 폴더 매핑
        registry.addResourceHandler("/images/board/**")
                .addResourceLocations("file:///" + boardImgLocation + "/")
                .setCachePeriod(3600)
                .resourceChain(true);
    }
}