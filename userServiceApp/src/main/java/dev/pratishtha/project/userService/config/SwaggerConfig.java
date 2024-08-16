package dev.pratishtha.project.userService.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(title = "User Service Api",
                                description = "This API documentation covers all the apis related to user management.",
                                summary = "API containing user information."),
                                servers = {@Server(description = "testEnv", url = "http://localhost:9101/")})
public class SwaggerConfig {

    
}
