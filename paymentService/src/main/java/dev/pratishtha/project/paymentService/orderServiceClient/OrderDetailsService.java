package dev.pratishtha.project.paymentService.orderServiceClient;

import dev.pratishtha.project.paymentService.exceptions.InvalidUserAuthenticationException;
import dev.pratishtha.project.paymentService.exceptions.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class OrderDetailsService {

    private RestTemplateBuilder restTemplateBuilder;
    private RestTemplate restTemplate;
    private String orderServiceBaseUrl;

    @Autowired
    public OrderDetailsService(RestTemplateBuilder restTemplateBuilder,
                               @Value("${order-service.api.baseurl}") String orderServiceBaseUrl) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.restTemplate = restTemplateBuilder.build();
        this.orderServiceBaseUrl = orderServiceBaseUrl;
    }

    public Optional<OrderResponseDto> getOrderDetails (String token, String orderId) {
        String getOrderDetailsUrl = orderServiceBaseUrl + "/" + orderId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity entity = new HttpEntity<>(headers);

        try {

            ResponseEntity<OrderResponseDto> responseEntity = restTemplate
                    .exchange(
                            getOrderDetailsUrl,
                            HttpMethod.GET,
                            entity,
                            OrderResponseDto.class
                    );

            if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                OrderResponseDto responseDto = responseEntity.getBody();

                return Optional.ofNullable(responseDto);
            }
            else {
                throw new OrderNotFoundException("Product with id - " + orderId + " not found.");
            }
        }
        catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND || e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                throw new OrderNotFoundException("Product with id - " + orderId + " not found.");
            }
            else if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new InvalidUserAuthenticationException("User token is not authenticated. Please enter a valid authentication token.");
            }
            else {
                throw e;
            }
        }
    }
}
