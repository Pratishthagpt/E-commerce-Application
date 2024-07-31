package dev.pratishtha.project.orderService.security;

import dev.pratishtha.project.orderService.exceptions.InvalidUserAuthenticationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
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

    public TokenValidator(RestTemplateBuilder restTemplateBuilder,
                          @Value("${user-service.api.baseurl}") String userBaseUrl,
                          @Value("${user-service.api.auth}") String userValidateUrl) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.restTemplate = restTemplate;
        this.userBaseUrl = userBaseUrl;
        this.userAuthenticationUrl = userBaseUrl + userValidateUrl;
    }

    public Optional<JwtData> validateToken (String token) {
        UserValidateDto userValidateDto = new UserValidateDto();
        userValidateDto.setToken(token);

        try {
            ResponseEntity<UserDto> responseEntity = restTemplate.postForEntity(
                userAuthenticationUrl, userValidateDto, UserDto.class
            );

            if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                UserDto userDto = responseEntity.getBody();

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
            else {
                throw e;
            }
        }
    }
}
