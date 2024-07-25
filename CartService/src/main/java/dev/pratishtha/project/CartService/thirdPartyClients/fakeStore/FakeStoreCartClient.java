package dev.pratishtha.project.CartService.thirdPartyClients.fakeStore;

import dev.pratishtha.project.CartService.thirdPartyClients.fakeStore.dtos.FakeStoreCartDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Component
public class FakeStoreCartClient {

//    private static final Logger LOGGER = Logger.getLogger(FakeStoreCartClient.class.getName());
    private RestTemplateBuilder restTemplateBuilder;

    private String cartRequestUrl;
    private RestTemplate restTemplate;

    @Autowired
    public FakeStoreCartClient(RestTemplateBuilder restTemplateBuilder,
                               @Value("${fakeStore.api.baseurl}") String fakeStoreBaseUrl,
                               @Value("${fakeStore.api.cart}") String fakeStoreCartUrl) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.cartRequestUrl = fakeStoreBaseUrl + fakeStoreCartUrl;

        this.restTemplate = restTemplateBuilder.build();
    }

//    Get all carts from FakeStoreApi
    public List<FakeStoreCartDTO> getAllCartsFromFakeStore() {

//        LOGGER.info("Requesting carts from URL: " + cartRequestUrl);

        ResponseEntity<FakeStoreCartDTO[]> responseEntity =
                restTemplate.getForEntity(cartRequestUrl, FakeStoreCartDTO[].class);

        FakeStoreCartDTO[] fakeStoreCartResponse = responseEntity.getBody();
        if (fakeStoreCartResponse == null) {
//            LOGGER.warning("Received null response from FakeStore API");
            return new ArrayList<>();
        }

//        LOGGER.info("Received response: " + Arrays.toString(fakeStoreCartResponse));

        List<FakeStoreCartDTO> fakeStoreCartDTOList = new ArrayList<>();
        if (fakeStoreCartResponse != null) {
            for (FakeStoreCartDTO fakeStoreCartDTO : fakeStoreCartResponse) {
                if (fakeStoreCartDTO != null && fakeStoreCartDTO.getCartItems() != null) {
                    fakeStoreCartDTOList.add(fakeStoreCartDTO);
                }
            }
        }

        return fakeStoreCartDTOList;
    }
}
