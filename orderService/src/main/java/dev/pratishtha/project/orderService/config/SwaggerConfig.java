package dev.pratishtha.project.orderService.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(title = "Order Service Api",
        description = "This API documentation covers all the apis related to order management.",
        summary = "API containing order and order items information."),
        servers = {@Server(description = "testEnv", url = "http://localhost:9103/")})
public class SwaggerConfig {

}
