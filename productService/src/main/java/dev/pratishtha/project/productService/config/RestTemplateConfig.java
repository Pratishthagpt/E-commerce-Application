package dev.pratishtha.project.productService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

// because it is from third party source and is not annotated with @Component, so spring cannot manage this bean and to make it managed by
// spring application context container, we configure it with @Configuration and @Bean
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
