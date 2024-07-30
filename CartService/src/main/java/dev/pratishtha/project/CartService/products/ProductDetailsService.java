package dev.pratishtha.project.CartService.products;

import dev.pratishtha.project.CartService.exceptions.InvalidUserAuthenticationException;
import dev.pratishtha.project.CartService.exceptions.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class ProductDetailsService {

    private RestTemplateBuilder restTemplateBuilder;
    private RestTemplate restTemplate;
    private String productBaseUrl;

    public ProductDetailsService(RestTemplateBuilder restTemplateBuilder,
                                 @Value("${product.api.baseurl}") String productBaseUrl) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.restTemplate = restTemplateBuilder.build();
        this.productBaseUrl = productBaseUrl;
    }

    public Optional<ProductDto> getProductFromProductService(String productId, String token) {

        String productRequestUrl = productBaseUrl + "/" + productId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<ProductDto> response = restTemplate.exchange(
                    productRequestUrl, HttpMethod.GET, entity,  ProductDto.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                ProductDto productDto = response.getBody();

//                System.out.println("ProductDto: " + productDto);

                return Optional.of(productDto);
            }
            else {
                throw new ProductNotFoundException("Product with id - " + productId + " not found.");
            }
        }
        catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND || e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                throw new ProductNotFoundException("Product with id - " + productId + " not found.");
            }
            else if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new InvalidUserAuthenticationException("User token is not authenticated. Please enter a valid authentication token.");
            }
            else throw e;
        }
    }
}
