package com.ejar.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

import java.util.List;

@Configuration
@PropertySource({"classpath:web.properties"})
@EnableWebMvc
@ComponentScans(value = {
    @ComponentScan("com.ejar.controller"),
    @ComponentScan("com.ejar.controller.error")
})
public class WebConfiguration implements WebMvcConfigurer {

    private final Logger log = LoggerFactory.getLogger(WebConfiguration.class);

    @Autowired
    private Environment env;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin(env.getProperty("cors.allowed_origins"));
        config.addAllowedMethod(env.getProperty("cors.allowed_methods"));
        config.addAllowedHeader(env.getProperty("cors.allowed_headers"));
        config.addExposedHeader(env.getProperty("cors.exposed_headers"));
        config.setAllowCredentials(Boolean.parseBoolean(env.getProperty("cors.allow_credentials")));
        config.setMaxAge(Long.parseLong(env.getProperty("cors.max_age")));

        if (config.getAllowedOrigins() != null && !config.getAllowedOrigins().isEmpty()) {
            log.debug("Registering CORS filter");
            source.registerCorsConfiguration("/api/**", config);
        }
        return new CorsFilter(source);
    }

    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        ObjectMapper mapper = Jackson2ObjectMapperBuilder
            .json()
            .modules(new ProblemModule(), new ConstraintViolationProblemModule())
            .build();

        converters.add(new MappingJackson2HttpMessageConverter(mapper));
    }
}
