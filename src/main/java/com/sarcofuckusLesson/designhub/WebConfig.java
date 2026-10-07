package com.sarcofuckusLesson.designhub;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Существующая настройка для uploads
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/uploads/");

        // Для 3Dtable — явно указываем, что по /3Dtable/** искать в static/3Dtable/
        registry.addResourceHandler("/3Dtable/**")
                .addResourceLocations("classpath:/static/3Dtable/");

        // Для всего остального — стандартная статика
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}

