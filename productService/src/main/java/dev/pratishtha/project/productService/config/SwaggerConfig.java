package dev.pratishtha.project.productService.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(title = "Product Service Api",
        description = "This API documentation covers all the apis related to product management.",
        summary = "API containing products information."),
        servers = {@Server(description = "testEnv", url = "http://localhost:9100/")})
public class SwaggerConfig {

}
