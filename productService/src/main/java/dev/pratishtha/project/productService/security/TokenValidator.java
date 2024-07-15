package dev.pratishtha.project.productService.security;

import dev.pratishtha.project.productService.exceptions.InvalidUserAuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class TokenValidator {
    private RestTemplateBuilder restTemplateBuilder;
    private RestTemplate restTemplate;
    private String userBaseUrl;
    private String userAuthenticationUrl;


    @Autowired
    public TokenValidator(RestTemplateBuilder restTemplateBuilder,
                          @Value("http://localhost:9101/users") String userBaseUrl,
                          @Value("/auth/validate") String userValidateUrl) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.restTemplate = restTemplateBuilder.build();
        this.userBaseUrl = userBaseUrl;
        this.userAuthenticationUrl = userBaseUrl + userValidateUrl;
    }

    public Optional<JwtData> validateToken (String token) throws InvalidUserAuthenticationException {
        UserValidateDto userValidateDto = new UserValidateDto();
        userValidateDto.setToken(token);

        try {
            ResponseEntity<UserDto> response = restTemplate.postForEntity(
                    userAuthenticationUrl, userValidateDto, UserDto.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                UserDto userDto = response.getBody();
                JwtData userJwtData = new JwtData();

                userJwtData.setUserId(userDto.getUserId());
                userJwtData.setEmail(userDto.getEmail());
                userJwtData.setUsername(userDto.getUsername());
                userJwtData.setRoles(new ArrayList<>(userDto.getRoles()));

                return Optional.of(userJwtData);
            }
            else {
                throw new InvalidUserAuthenticationException("User token is not Authenticated. Please enter the valid authentication token.");
            }
        }
        catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND || e.getStatusCode() == HttpStatus.UNAUTHORIZED
                    || e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                throw new InvalidUserAuthenticationException("User token is not Authenticated. Please enter the valid authentication token.");
            }
            else  {
                throw e;
            }
        }

    }
}
