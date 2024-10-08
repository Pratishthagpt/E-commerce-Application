package dev.pratishtha.project.productService.thirdPartyClents.fakeStore;

import dev.pratishtha.project.productService.exceptions.CategoryNotFoundException;
import dev.pratishtha.project.productService.exceptions.IdNotFoundException;
import dev.pratishtha.project.productService.thirdPartyClents.fakeStore.dtos.FakeStoreProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
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
    private String categoryRequestUrl;
    private RestTemplate restTemplate;

    @Autowired
    public FakeStoreProductClient(RestTemplateBuilder restTemplateBuilder,
                                  @Value("${fakestore.api.baseurl}") String fakeStoreBaseUrl,
                                  @Value("${fakestore.api.product}") String fakeStoreProductUrl,
                                  @Value("${fakestore.api.category}") String fakeStoreCategoryUrl) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.productUrl = fakeStoreBaseUrl + fakeStoreProductUrl + "/{id}";
        this.productRequestUrl = fakeStoreBaseUrl + fakeStoreProductUrl;
        this.categoryRequestUrl = fakeStoreBaseUrl + fakeStoreProductUrl + fakeStoreCategoryUrl;

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

    //    For Get a list of products with limit
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

    //    For Get a list of products as per sorting type
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


//    For Get a list of categories in product
    public List<String> getAllCategoriesFromFakeStore() {

        ResponseEntity<String[]> responseEntity =
                restTemplate.getForEntity(categoryRequestUrl, String[].class);

//        because response entity contains status code, headers, body, so we extract body from response entity
        String[] fakeStoreCategoryResponse = responseEntity.getBody();

//        converting array to list
        List<String> fakeStoreCategoryList = new ArrayList<>();

        for (String product : fakeStoreCategoryResponse) {
            fakeStoreCategoryList.add(product);
        }
        return fakeStoreCategoryList;
    }


//    For Get a list of products by category
    public List<FakeStoreProductDTO> getAllProductsByCategoryFromFakeStore(String category) throws CategoryNotFoundException {
        String productsByCategoryUrl = productRequestUrl + "/category/" + category;

        ResponseEntity<FakeStoreProductDTO[]> responseEntity = restTemplate.getForEntity(
                productsByCategoryUrl, FakeStoreProductDTO[].class, category);

        FakeStoreProductDTO[] fakeStoreProductResponse = responseEntity.getBody();

        if (fakeStoreProductResponse.length == 0) {
            throw new CategoryNotFoundException("Category with name - " + category + " not found.");
        }

//        converting array to list
        List<FakeStoreProductDTO> fakeStoreProductsList = new ArrayList<>();

        for (FakeStoreProductDTO product : fakeStoreProductResponse) {
            fakeStoreProductsList.add(product);
        }
        return fakeStoreProductsList;

    }


//    For creating a new product
    public FakeStoreProductDTO createNewProductInFakeStore(FakeStoreProductDTO fakeStoreProductRequest) {

        ResponseEntity<FakeStoreProductDTO> responseEntity = restTemplate.postForEntity(
                productRequestUrl, fakeStoreProductRequest, FakeStoreProductDTO.class
        );

//        Remember that nothing in real will insert into the fakestore database. so if we access the new id we will get a 404 error.


        FakeStoreProductDTO fakeStoreProductResponse = responseEntity.getBody();

        return fakeStoreProductResponse;
    }

//    For updating a product
    public FakeStoreProductDTO updateProductById(String id, FakeStoreProductDTO fakeStoreProductRequest) {

//        product request url
        String productPath = productRequestUrl + "/" + id;

//        getting response entity wrapping around object as response from third part api while hitting its url and also takes id as parameter
//        using exchange method here, because put method returns void
//        remember that nothing in real will update in the database of fake store API
        ResponseEntity<FakeStoreProductDTO> responseEntity =
                restTemplate.exchange(productPath,
                        HttpMethod.PUT,
                        new HttpEntity<>(fakeStoreProductRequest),
                        FakeStoreProductDTO.class,
                        id);

        FakeStoreProductDTO updatedFakeStoreProduct = responseEntity.getBody();

        return updatedFakeStoreProduct;
    }

//    for updating part of product
    public FakeStoreProductDTO updateSubProductById(String id, FakeStoreProductDTO fakeStoreProductRequest) {
        RestTemplate restTemplate_1 = restTemplateBuilder
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build();

//        getting response entity wrapping around object as response from third part api while hitting its url and also takes id as parameter
//        using exchange method here, because patch method returns void
//        remember that nothing in real will update in the database of fake store API
        ResponseEntity<FakeStoreProductDTO> responseEntity =
                restTemplate_1.exchange(productUrl,
                        HttpMethod.PATCH,
                        new HttpEntity<>(fakeStoreProductRequest),
                        FakeStoreProductDTO.class,
                        id);

        FakeStoreProductDTO updatedFakeStoreProduct = responseEntity.getBody();

        return updatedFakeStoreProduct;

    }

//    delete the product
    public FakeStoreProductDTO deleteProductById(String id) {

        ResponseEntity<FakeStoreProductDTO> responseEntity =
                restTemplate.exchange(productUrl,
                        HttpMethod.DELETE,
                        null,
                        FakeStoreProductDTO.class,
                        id
                        );

        FakeStoreProductDTO deletedFakeStoreProduct = responseEntity.getBody();

        return deletedFakeStoreProduct;
    }
}
