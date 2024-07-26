package dev.pratishtha.project.CartService.services;

import dev.pratishtha.project.CartService.dtos.DateRangeDTO;
import dev.pratishtha.project.CartService.dtos.GenericCartItemDTO;
import dev.pratishtha.project.CartService.dtos.GenericCartDTO;
import dev.pratishtha.project.CartService.thirdPartyClients.fakeStore.FakeStoreCartClient;
import dev.pratishtha.project.CartService.thirdPartyClients.fakeStore.dtos.FakeStoreCartDTO;
import dev.pratishtha.project.CartService.thirdPartyClients.fakeStore.dtos.FakeStoreCartItemDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FakeStoreCartServiceImpl implements CartService{

//    private static final Logger LOGGER = Logger.getLogger(FakeStoreCartServiceImpl.class.getName());
    private FakeStoreCartClient fakeStoreCartClient;

    public FakeStoreCartServiceImpl(FakeStoreCartClient fakeStoreCartClient) {
        this.fakeStoreCartClient = fakeStoreCartClient;
    }

    @Override
    public List<GenericCartDTO> getAllCarts() {
        List<FakeStoreCartDTO> fakeStoreCarts = fakeStoreCartClient.getAllCartsFromFakeStore();

//        LOGGER.info("Converting FakeStoreCartDTO to GenericCartDTO");
        List<GenericCartDTO> genericCartDTOS = new ArrayList<>();
        for (FakeStoreCartDTO fakeStoreCartDTO : fakeStoreCarts) {
            genericCartDTOS.add(convertFakeStoreCartDtoToGenericCartDto(fakeStoreCartDTO));
        }

//        LOGGER.info("Conversion complete. Returning " + genericCartDTOS.size() + " carts.");

        return genericCartDTOS;
    }

    @Override
    public GenericCartDTO addNewCart(GenericCartDTO requestDto) {
        FakeStoreCartDTO fakeStoreCartRequest =
                convertGenericCartDtoToFakeStoreCartDto(requestDto);

        FakeStoreCartDTO fakeStoreCartDTO = fakeStoreCartClient.addNewCartToFakeStore(fakeStoreCartRequest);

        GenericCartDTO genericCartDTO = convertFakeStoreCartDtoToGenericCartDto(fakeStoreCartDTO);

        return genericCartDTO;
    }

    @Override
    public GenericCartDTO getCartById(String cartId) {

        FakeStoreCartDTO fakeStoreCartDTO = fakeStoreCartClient.getSingleCartByIdFromFakeStore(cartId);

        GenericCartDTO genericCartDTO = convertFakeStoreCartDtoToGenericCartDto(fakeStoreCartDTO);

        return genericCartDTO;
    }

    @Override
    public List<GenericCartDTO> getCartsByLimit(int limit) {

        List<FakeStoreCartDTO> fakeStoreCarts = fakeStoreCartClient.getCartsByLimitFromFakeStore(limit);

        List<GenericCartDTO> genericCartDTOS = new ArrayList<>();
        for (FakeStoreCartDTO fakeStoreCartDTO : fakeStoreCarts) {
            genericCartDTOS.add(convertFakeStoreCartDtoToGenericCartDto(fakeStoreCartDTO));
        }

        return genericCartDTOS;
    }

    @Override
    public List<GenericCartDTO> getCartsBySort(String sortType) {
        List<FakeStoreCartDTO> fakeStoreCarts = fakeStoreCartClient.getAllCartsWithSortFromFakeStore(sortType);

        List<GenericCartDTO> genericCartDTOS = new ArrayList<>();
        for (FakeStoreCartDTO fakeStoreCartDTO : fakeStoreCarts) {
            genericCartDTOS.add(convertFakeStoreCartDtoToGenericCartDto(fakeStoreCartDTO));
        }

        return genericCartDTOS;
    }

    @Override
    public List<GenericCartDTO> getCartsInDateRange(DateRangeDTO dateRangeDTO) {
        List<FakeStoreCartDTO> fakeStoreCarts = fakeStoreCartClient.getCartsWithinDateRangeFromFakeStore(dateRangeDTO.getStartDate(), dateRangeDTO.getEndDate());

        List<GenericCartDTO> genericCartDTOS = new ArrayList<>();
        for (FakeStoreCartDTO fakeStoreCartDTO : fakeStoreCarts) {
            genericCartDTOS.add(convertFakeStoreCartDtoToGenericCartDto(fakeStoreCartDTO));
        }

        return genericCartDTOS;
    }

    @Override
    public List<GenericCartDTO> getSortedCartsInDateRangeWithLimit(DateRangeDTO dateRangeDTO, int limit, String sortType) {
        List<FakeStoreCartDTO> fakeStoreCarts = fakeStoreCartClient.
                getSortedCartsInDateRangeWithLimitFromFakeStore(dateRangeDTO.getStartDate(),
                        dateRangeDTO.getEndDate(), limit, sortType);

        List<GenericCartDTO> genericCartDTOS = new ArrayList<>();
        for (FakeStoreCartDTO fakeStoreCartDTO : fakeStoreCarts) {
            genericCartDTOS.add(convertFakeStoreCartDtoToGenericCartDto(fakeStoreCartDTO));
        }

        return genericCartDTOS;
    }

    private GenericCartDTO convertFakeStoreCartDtoToGenericCartDto (FakeStoreCartDTO fakeStoreCartDTO) {
        GenericCartDTO genericCartDTO = new GenericCartDTO();

        List<GenericCartItemDTO> genericCartItemDTOS = new ArrayList<>();
        for (FakeStoreCartItemDTO cartItemDTO : fakeStoreCartDTO.getProducts()) {
            genericCartItemDTOS.add(convertFakeStoreCartItemDtoToGenericGenericCartItemDto(cartItemDTO));
        }

        genericCartDTO.setCartItems(genericCartItemDTOS);
        genericCartDTO.setUserId(fakeStoreCartDTO.getUserId());
        genericCartDTO.setCreatedAt(fakeStoreCartDTO.getDate());
        genericCartDTO.setCartId(fakeStoreCartDTO.getId());
        genericCartDTO.setTotalItems(genericCartItemDTOS.size());

        return genericCartDTO;
    }

    private GenericCartItemDTO convertFakeStoreCartItemDtoToGenericGenericCartItemDto (FakeStoreCartItemDTO fakeStoreCartItemDTO) {
        GenericCartItemDTO genericCartItemDTO = new GenericCartItemDTO();
        genericCartItemDTO.setProductId(fakeStoreCartItemDTO.getProductId());
        genericCartItemDTO.setQuantity(fakeStoreCartItemDTO.getQuantity());

        return genericCartItemDTO;
    }

    private FakeStoreCartDTO convertGenericCartDtoToFakeStoreCartDto (GenericCartDTO genericCartDTO) {
        FakeStoreCartDTO fakeStoreCartDTO = new FakeStoreCartDTO();

        List<FakeStoreCartItemDTO> fakeStoreCartItemDTOS = new ArrayList<>();
        for (GenericCartItemDTO cartItemDTO : genericCartDTO.getCartItems()) {
            fakeStoreCartItemDTOS.add(convertGenericCartItemDtoToFakeStoreCartItemDto(cartItemDTO));
        }

        fakeStoreCartDTO.setProducts(fakeStoreCartItemDTOS);
        fakeStoreCartDTO.setUserId(genericCartDTO.getUserId());
        fakeStoreCartDTO.setDate(genericCartDTO.getCreatedAt());
        fakeStoreCartDTO.setId(genericCartDTO.getCartId());

        return fakeStoreCartDTO;
    }

    private FakeStoreCartItemDTO convertGenericCartItemDtoToFakeStoreCartItemDto (GenericCartItemDTO genericCartItemDTO) {
        FakeStoreCartItemDTO fakeStoreCartItemDTO = new FakeStoreCartItemDTO();
        fakeStoreCartItemDTO.setProductId(genericCartItemDTO.getProductId());
        fakeStoreCartItemDTO.setQuantity(genericCartItemDTO.getQuantity());

        return fakeStoreCartItemDTO;
    }
}
