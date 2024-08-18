package dev.pratishtha.project.paymentService.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(title = "Payment Service Api",
        description = "This API documentation covers all the apis related to payment processing.",
        summary = "API containing all payment related information."),
        servers = {@Server(description = "testEnv", url = "http://localhost:9104/")})
public class SwaggerConfig {

}
