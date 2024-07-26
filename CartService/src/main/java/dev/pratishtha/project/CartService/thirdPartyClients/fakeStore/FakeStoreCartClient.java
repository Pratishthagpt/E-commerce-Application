package dev.pratishtha.project.CartService.thirdPartyClients.fakeStore;

import dev.pratishtha.project.CartService.exceptions.CartIdNotFoundException;
import dev.pratishtha.project.CartService.thirdPartyClients.fakeStore.dtos.FakeStoreCartDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class FakeStoreCartClient {

//    private static final Logger LOGGER = Logger.getLogger(FakeStoreCartClient.class.getName());
    private RestTemplateBuilder restTemplateBuilder;

    private String fakeStoreCartRequestUrl;
    private RestTemplate restTemplate;
    private String fakeStoreCartUrl;

    @Autowired
    public FakeStoreCartClient(RestTemplateBuilder restTemplateBuilder,
                               @Value("${fakeStore.api.baseurl}") String fakeStoreBaseUrl,
                               @Value("${fakeStore.api.cart}") String fakeStoreCartUrl) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.fakeStoreCartRequestUrl = fakeStoreBaseUrl + fakeStoreCartUrl;
        this.fakeStoreCartUrl = fakeStoreBaseUrl + fakeStoreCartUrl + "/{id}";

        this.restTemplate = restTemplateBuilder.build();
    }

//    Get all carts from FakeStoreApi
    public List<FakeStoreCartDTO> getAllCartsFromFakeStore() {

//        LOGGER.info("Requesting carts from URL: " + cartRequestUrl);

        ResponseEntity<FakeStoreCartDTO[]> responseEntity =
                restTemplate.getForEntity(fakeStoreCartRequestUrl, FakeStoreCartDTO[].class);

        FakeStoreCartDTO[] fakeStoreCartResponse = responseEntity.getBody();
        if (fakeStoreCartResponse == null) {
//            LOGGER.warning("Received null response from FakeStore API");
            return new ArrayList<>();
        }

//        LOGGER.info("Received response: " + Arrays.toString(fakeStoreCartResponse));

        List<FakeStoreCartDTO> fakeStoreCartDTOList = new ArrayList<>();
        if (fakeStoreCartResponse != null) {
            for (FakeStoreCartDTO fakeStoreCartDTO : fakeStoreCartResponse) {
                if (fakeStoreCartDTO != null || fakeStoreCartDTO.getProducts() != null) {
                    fakeStoreCartDTOList.add(fakeStoreCartDTO);
                }
            }
        }

        return fakeStoreCartDTOList;
    }

    public FakeStoreCartDTO addNewCartToFakeStore(FakeStoreCartDTO fakeStoreCartRequest) {

        ResponseEntity<FakeStoreCartDTO> responseEntity =
                restTemplate.postForEntity(fakeStoreCartRequestUrl, fakeStoreCartRequest, FakeStoreCartDTO.class);

        FakeStoreCartDTO fakeStoreCartDTO = responseEntity.getBody();

        if (fakeStoreCartDTO != null || fakeStoreCartDTO.getProducts() != null) {
            return fakeStoreCartDTO;
        }
        return new FakeStoreCartDTO();
    }

    public FakeStoreCartDTO getSingleCartByIdFromFakeStore(String cartId) {

        ResponseEntity<FakeStoreCartDTO> responseEntity =
                restTemplate.getForEntity(fakeStoreCartUrl, FakeStoreCartDTO.class, cartId);

        FakeStoreCartDTO fakeStoreCartResponse = responseEntity.getBody();

        if (fakeStoreCartResponse == null) {
            throw new CartIdNotFoundException("Cart with Id - " + cartId + " not found");
        }

        return fakeStoreCartResponse;
    }


    public List<FakeStoreCartDTO> getCartsByLimitFromFakeStore(int limit) {

        String cartsRequestByLimitUrl = fakeStoreCartRequestUrl + "?limit=" + limit;

        ResponseEntity<FakeStoreCartDTO[]> responseEntity =
                restTemplate.getForEntity(cartsRequestByLimitUrl, FakeStoreCartDTO[].class, limit);

        FakeStoreCartDTO[] fakeStoreCartResponse = responseEntity.getBody();
        if (fakeStoreCartResponse == null) {
            return new ArrayList<>();
        }

        List<FakeStoreCartDTO> fakeStoreCartDTOList = new ArrayList<>();
        if (fakeStoreCartResponse != null) {
            for (FakeStoreCartDTO fakeStoreCartDTO : fakeStoreCartResponse) {
                if (fakeStoreCartDTO != null || fakeStoreCartDTO.getProducts() != null) {
                    fakeStoreCartDTOList.add(fakeStoreCartDTO);
                }
            }
        }

        return fakeStoreCartDTOList;
    }

    public List<FakeStoreCartDTO> getAllCartsWithSortFromFakeStore(String sortType) {
        String sort = "asc";

        if (sortType.equalsIgnoreCase("descending") || sortType.equalsIgnoreCase("desc")) {
            sort = "desc";
        }

        String cartsRequestBySortUrl = fakeStoreCartRequestUrl + "?sort=" + sort;

        ResponseEntity<FakeStoreCartDTO[]> responseEntity =
                restTemplate.getForEntity(cartsRequestBySortUrl, FakeStoreCartDTO[].class);

        FakeStoreCartDTO[] fakeStoreCartResponse = responseEntity.getBody();
        if (fakeStoreCartResponse == null) {
            return new ArrayList<>();
        }

        List<FakeStoreCartDTO> fakeStoreCartDTOList = new ArrayList<>();
        if (fakeStoreCartResponse != null) {
            for (FakeStoreCartDTO fakeStoreCartDTO : fakeStoreCartResponse) {
                if (fakeStoreCartDTO != null || fakeStoreCartDTO.getProducts() != null) {
                    fakeStoreCartDTOList.add(fakeStoreCartDTO);
                }
            }
        }

        return fakeStoreCartDTOList;
    }
}
