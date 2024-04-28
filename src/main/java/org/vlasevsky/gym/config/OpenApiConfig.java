package org.vlasevsky.gym.config;


import io.swagger.v3.oas.models.OpenAPI;
import lombok.AllArgsConstructor;
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.core.configuration.SpringDocDataRestConfiguration;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.webmvc.core.configuration.SpringDocWebMvcConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.vlasevsky.gym.interceptor.LoggingInterceptor;

@EnableWebMvc
@EnableAutoConfiguration(exclude = SpringDocDataRestConfiguration.class)
@ComponentScan(basePackages = {"org.springdoc", "org.vlasevsky.gym"})
@Import({SpringDocConfiguration.class,
        SpringDocWebMvcConfiguration.class,
        org.springdoc.webmvc.ui.SwaggerConfig.class,
        SwaggerUiConfigProperties.class,
        SwaggerUiOAuthProperties.class,
        JacksonAutoConfiguration.class})
@Configuration
@AllArgsConstructor
public class OpenApiConfig implements WebMvcConfigurer {

    @Autowired
    private LoggingInterceptor loggingInterceptor;


    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI();
    }


    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("gym")
                .packagesToScan("org.vlasevsky.gym.controller")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public SwaggerUiConfigProperties swaggerUiConfigProperties() {
        SwaggerUiConfigProperties config = new SwaggerUiConfigProperties();
        config.setConfigUrl("/api/v3/api-docs/swagger-config");
        return config;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor);
    }


}

