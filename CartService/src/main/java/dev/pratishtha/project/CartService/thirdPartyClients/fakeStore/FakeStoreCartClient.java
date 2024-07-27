package dev.pratishtha.project.CartService.thirdPartyClients.fakeStore;

import dev.pratishtha.project.CartService.exceptions.CartIdNotFoundException;
import dev.pratishtha.project.CartService.exceptions.CartNotPresentException;
import dev.pratishtha.project.CartService.thirdPartyClients.fakeStore.dtos.FakeStoreCartDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        if (fakeStoreCartResponse.length == 0) {
            throw new CartNotPresentException("Carts not found.");
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
        if (fakeStoreCartResponse.length == 0) {
            throw new CartNotPresentException("Carts not found.");
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
        if (fakeStoreCartResponse.length == 0) {
            throw new CartNotPresentException("Carts not found.");
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

    public List<FakeStoreCartDTO> getCartsWithinDateRangeFromFakeStore(LocalDate startDate, LocalDate endDate) {
        if (startDate == null) {
            startDate = LocalDate.of(1970, 01, 01);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDateInString = startDate.format(formatter);
        String endDateInString = endDate.format(formatter);

        String cartsRequestByDateRangeUrl = fakeStoreCartRequestUrl + "?startdate=" + startDateInString + "&" + "enddate=" + endDateInString;

        ResponseEntity<FakeStoreCartDTO[]> responseEntity =
                restTemplate.getForEntity(cartsRequestByDateRangeUrl, FakeStoreCartDTO[].class);

        FakeStoreCartDTO[] fakeStoreCartResponse = responseEntity.getBody();
        if (fakeStoreCartResponse.length == 0) {
            throw new CartNotPresentException("Carts not found.");
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

    public List<FakeStoreCartDTO> getSortedCartsInDateRangeWithLimitFromFakeStore
            (LocalDate startDate, LocalDate endDate, int limit, String sortType) {

        if (startDate == null) {
            startDate = LocalDate.of(1970, 01, 01);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        String sort = "asc";

        if (sortType.equalsIgnoreCase("descending") || sortType.equalsIgnoreCase("desc")) {
            sort = "desc";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDateInString = startDate.format(formatter);
        String endDateInString = endDate.format(formatter);

        String cartsRequestByDateRangeWithLimitAndSortUrl =
                fakeStoreCartRequestUrl + "?startdate=" + startDateInString + "&enddate=" + endDateInString + "&sort=" + sort +
                        "&limit=" + limit;

        ResponseEntity<FakeStoreCartDTO[]> responseEntity =
                restTemplate.getForEntity(cartsRequestByDateRangeWithLimitAndSortUrl, FakeStoreCartDTO[].class, limit);

        FakeStoreCartDTO[] fakeStoreCartResponse = responseEntity.getBody();
        if (fakeStoreCartResponse.length == 0) {
            throw new CartNotPresentException("Carts not found.");
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

    public List<FakeStoreCartDTO> getCartsByUserFromFakeStore(String userId) {

        String cartRequestByUserUrl = fakeStoreCartRequestUrl + "/user/" + userId;

        ResponseEntity<FakeStoreCartDTO[]> responseEntity =
                restTemplate.getForEntity(cartRequestByUserUrl, FakeStoreCartDTO[].class, userId);

        FakeStoreCartDTO[] fakeStoreCartResponse = responseEntity.getBody();
        System.out.println(fakeStoreCartResponse.length);

        if (fakeStoreCartResponse.length == 0) {
            throw new CartNotPresentException("Cart for user with userId - " + userId + " not present.");
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
