package dev.pratishtha.project.CartService.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(title = "Cart Service Api",
        description = "This API documentation covers all the apis related to cart management.",
        summary = "API containing cart and cart items information."),
        servers = {@Server(description = "testEnv", url = "http://localhost:9102/")})
public class SwaggerConfig {

}
