package dev.pratishtha.project.productService.thirdPartyClents.fakeStore;

import dev.pratishtha.project.productService.dtos.GenericProductDTO;
import dev.pratishtha.project.productService.thirdPartyClents.fakeStore.dtos.FakeStoreProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

// Client class contains all the separate business logic to deal with third party fake store API.
// This is done to remove all the third party logic from service class.

@Component
public class FakeStoreProductClient {

    private RestTemplateBuilder restTemplateBuilder;

    private String productUrl;
    private String productRequestUrl;

    @Autowired
    public FakeStoreProductClient(RestTemplateBuilder restTemplateBuilder,
                                  @Value("${fakestore.api.baseurl}") String fakeStoreBaseUrl,
                                  @Value("${fakestore.api.product}") String fakeStoreProductUrl) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.productUrl = fakeStoreBaseUrl + fakeStoreProductUrl + "/{id}";
        this.productRequestUrl = fakeStoreBaseUrl + fakeStoreProductUrl;
    }


    public List<FakeStoreProductDTO> getAllProductsFromFakeStore() {
//        build rest template object using restTemplateBuilder
        RestTemplate restTemplate = restTemplateBuilder.build();

//        getting response entity as response from third part api while hitting its url
        ResponseEntity<FakeStoreProductDTO[]> responseEntity =
                restTemplate.getForEntity(productRequestUrl, FakeStoreProductDTO[].class);

//        because response entity contains status code, headers, body, so we extract body from response entity
        FakeStoreProductDTO[] fakeStoreProductResponse = responseEntity.getBody();

//        converting array to list
        List<FakeStoreProductDTO> fakeStoreProductsList = new ArrayList<>();

        for (FakeStoreProductDTO product : fakeStoreProductResponse) {
            fakeStoreProductsList.add(product);
        }

        return fakeStoreProductsList;
    }
}
