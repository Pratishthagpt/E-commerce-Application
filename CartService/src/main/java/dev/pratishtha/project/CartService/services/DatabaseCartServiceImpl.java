package dev.pratishtha.project.CartService.services;

import dev.pratishtha.project.CartService.dtos.DateRangeDTO;
import dev.pratishtha.project.CartService.dtos.GenericCartDTO;
import dev.pratishtha.project.CartService.dtos.GenericCartItemDTO;
import dev.pratishtha.project.CartService.models.Cart;
import dev.pratishtha.project.CartService.models.CartItem;
import dev.pratishtha.project.CartService.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class DatabaseCartServiceImpl implements CartService {

    private CartRepository cartRepository;

    @Autowired
    public DatabaseCartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }


    @Override
    public List<GenericCartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();

        List<GenericCartDTO> genericCartDtosList = new ArrayList<>();
        for (Cart cart : carts) {
            genericCartDtosList.add(convertCartToGenericCartDto(cart));
        }
        return genericCartDtosList;
    }


    @Override
    public GenericCartDTO addNewCart(GenericCartDTO requestDto) {
        return null;
    }

    @Override
    public GenericCartDTO getCartById(String cartId) {
        return null;
    }

    @Override
    public List<GenericCartDTO> getCartsByLimit(int limit) {
        return List.of();
    }

    @Override
    public List<GenericCartDTO> getCartsBySort(String sortType) {
        return List.of();
    }

    @Override
    public List<GenericCartDTO> getCartsInDateRange(DateRangeDTO dateRangeDTO) {
        return List.of();
    }

    @Override
    public List<GenericCartDTO> getSortedCartsInDateRangeWithLimit(DateRangeDTO dateRangeDTO, int limit, String sortType) {
        return List.of();
    }

    @Override
    public List<GenericCartDTO> getCartsByUser(String userId) {
        return List.of();
    }

    @Override
    public List<GenericCartDTO> getCartsByUserInDateRange(String userId, DateRangeDTO dateRangeDTO) {
        return List.of();
    }

    @Override
    public GenericCartDTO updateCartById(String id, GenericCartDTO requestDto) {
        return null;
    }

    @Override
    public GenericCartDTO updateSubCartById(String id, GenericCartDTO requestDto) {
        return null;
    }

    @Override
    public GenericCartDTO deleteCartById(String id) {
        return null;
    }

    private GenericCartDTO convertCartToGenericCartDto(Cart cart) {
        GenericCartDTO genericCartDTO = new GenericCartDTO();

        genericCartDTO.setCartId(String.valueOf(cart.getUuid()));
        genericCartDTO.setUserId(cart.getUserId());
        genericCartDTO.setTotalItems(cart.getTotalItems());
        genericCartDTO.setTotalPrice(cart.getTotalPrice());
        genericCartDTO.setCreatedAt(cart.getCreatedAt());

        List<GenericCartItemDTO> genericCartItemDTOS = new ArrayList<>();
        for (CartItem cartItem : cart.getCartItems()) {
            genericCartItemDTOS.add(convertCartItemToGenericCartItemDto(cartItem));
        }

        genericCartDTO.setCartItems(genericCartItemDTOS);

        return genericCartDTO;
    }

    private GenericCartItemDTO convertCartItemToGenericCartItemDto(CartItem cartItem) {
        GenericCartItemDTO genericCartItemDTO = new GenericCartItemDTO();

        genericCartItemDTO.setCartItemId(String.valueOf(cartItem.getUuid()));
        genericCartItemDTO.setItemAddedAt(cartItem.getItemAddedAt());
        genericCartItemDTO.setQuantity(cartItem.getQuantity());
        genericCartItemDTO.setProductId(cartItem.getProductId());
        genericCartItemDTO.setPrice(cartItem.getPrice());

        return genericCartItemDTO;
    }
}
