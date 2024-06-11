package dev.pratishtha.project.productService.thirdPartyClents.fakeStore;

import dev.pratishtha.project.productService.exceptions.IdNotFoundException;
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
    private RestTemplate restTemplate;

    @Autowired
    public FakeStoreProductClient(RestTemplateBuilder restTemplateBuilder,
                                  @Value("${fakestore.api.baseurl}") String fakeStoreBaseUrl,
                                  @Value("${fakestore.api.product}") String fakeStoreProductUrl) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.productUrl = fakeStoreBaseUrl + fakeStoreProductUrl + "/{id}";
        this.productRequestUrl = fakeStoreBaseUrl + fakeStoreProductUrl;

//        creating the object of rest template
        this.restTemplate = restTemplateBuilder.build();
    }

//  For Get all products

    public List<FakeStoreProductDTO> getAllProductsFromFakeStore() {

//        getting response entity wrapping around object as response from third part api while hitting its url
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

//    For Get a single product

    public FakeStoreProductDTO getProductById(String id) throws IdNotFoundException {

//        product request url
        String productPath = productRequestUrl + "/" + id;

//        getting response entity wrapping around object as response from third part api while hitting its url and also takes id as parameter
        ResponseEntity<FakeStoreProductDTO> responseEntity =
                restTemplate.getForEntity(productPath, FakeStoreProductDTO.class, id);

        FakeStoreProductDTO fakeStoreProductDTO = responseEntity.getBody();

        if (fakeStoreProductDTO == null) {
            throw new IdNotFoundException("Product with id - " + id + " not found.");
        }

        return fakeStoreProductDTO;

    }

    public List<FakeStoreProductDTO> getAllProductsWithLimitFromFakeStore(int limit) {

        String productUrlWithLimit = productRequestUrl + "?limit=" + limit;

        ResponseEntity<FakeStoreProductDTO[]> responseEntity = restTemplate.getForEntity(
                productUrlWithLimit, FakeStoreProductDTO[].class);

//        because response entity contains status code, headers, body, so we extract body from response entity
        FakeStoreProductDTO[] fakeStoreProductResponse = responseEntity.getBody();

//        converting array to list
        List<FakeStoreProductDTO> fakeStoreProductsList = new ArrayList<>();

        for (FakeStoreProductDTO product : fakeStoreProductResponse) {
            fakeStoreProductsList.add(product);
        }
        return fakeStoreProductsList;
    }

    public List<FakeStoreProductDTO> getAllProductsWithSortingFromFakeStore(String sortType) {
        String sort = "asc";

        if (sortType.equalsIgnoreCase("descending") || sortType.equalsIgnoreCase("desc")) {
            sort = "desc";
        }

        String productUrlWithSortType = productRequestUrl + "?sort=" + sort;

        ResponseEntity<FakeStoreProductDTO[]> responseEntity = restTemplate.getForEntity(
                productUrlWithSortType, FakeStoreProductDTO[].class);

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
