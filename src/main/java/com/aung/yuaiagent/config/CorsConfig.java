package com.aung.yuaiagent.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Global CORS configuration
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                // allow to send cookies from browser
                .allowCredentials(true)
                // allow to send request from any origin(should be patterns,
                // if not *, it will have an error with allowCredentials)
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .exposedHeaders("*");
//                .maxAge(3600);
    }
}
