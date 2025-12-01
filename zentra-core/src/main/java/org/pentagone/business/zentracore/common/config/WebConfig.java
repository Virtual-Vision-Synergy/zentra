package org.pentagone.business.zentracore.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration Web pour assurer que les routes AI sont traitées comme des endpoints API
 * et non comme des ressources statiques
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseTrailingSlashMatch(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configure uniquement les ressources statiques pour /static/**
        // Cela empêche Spring de traiter /ai/** comme des ressources statiques
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}

