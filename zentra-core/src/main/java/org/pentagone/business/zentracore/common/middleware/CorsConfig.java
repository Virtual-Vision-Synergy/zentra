package org.pentagone.business.zentracore.common.middleware;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("*") // autorise tout domaine
                        .allowedMethods("*") // autorise toutes les méthodes HTTP
                        .allowedHeaders("*")
                        .allowCredentials(true); // à garder si tu envoies des cookies ou Authorization
            }
        };
    }
}
