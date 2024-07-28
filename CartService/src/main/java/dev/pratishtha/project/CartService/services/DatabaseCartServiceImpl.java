package dev.pratishtha.project.CartService.services;

import dev.pratishtha.project.CartService.dtos.DateRangeDTO;
import dev.pratishtha.project.CartService.dtos.GenericCartDTO;
import dev.pratishtha.project.CartService.dtos.GenericCartItemDTO;
import dev.pratishtha.project.CartService.exceptions.CartIdNotFoundException;
import dev.pratishtha.project.CartService.models.Cart;
import dev.pratishtha.project.CartService.models.CartItem;
import dev.pratishtha.project.CartService.repositories.CartItemRepository;
import dev.pratishtha.project.CartService.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class DatabaseCartServiceImpl implements CartService {

    private CartRepository cartRepository;
    private CartItemRepository cartItemRepository;

    @Autowired
    public DatabaseCartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
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
        Cart cart = new Cart();

        List<GenericCartItemDTO> genericCartItemRequest = requestDto.getCartItems();
        List<CartItem> cartItems = new ArrayList<>();

        int totalPrice = 0;

        for (GenericCartItemDTO genericCartItemDTO : genericCartItemRequest) {
            CartItem cartItem = new CartItem();
            cartItem.setProductId(genericCartItemDTO.getProductId());
            cartItem.setItemAddedAt(genericCartItemDTO.getItemAddedAt());
            cartItem.setPrice(genericCartItemDTO.getPrice());
            cartItem.setQuantity(genericCartItemDTO.getQuantity());

            CartItem savedCartItem = cartItemRepository.save(cartItem);

            cartItems.add(savedCartItem);

            totalPrice += (savedCartItem.getPrice() * savedCartItem.getQuantity());
        }

        cart.setCartItems(cartItems);
        cart.setUserId(requestDto.getUserId());
        cart.setCreatedAt(new Date());
        cart.setTotalItems(cartItems.size());
        cart.setTotalPrice(totalPrice);

        Cart savedCart = cartRepository.save(cart);

        GenericCartDTO addedCart = convertCartToGenericCartDto(savedCart);

        return addedCart;
    }

    @Override
    public GenericCartDTO getCartById(String cartId) {
        UUID id = UUID.fromString(cartId);

        Optional<Cart> cartOptional = cartRepository.findById(id);
        if (cartOptional.isEmpty()) {
            throw new CartIdNotFoundException("Cart with id - " + id + " not found.");
        }

        Cart cart = cartOptional.get();

        GenericCartDTO genericCartDTO = convertCartToGenericCartDto(cart);
        return genericCartDTO;
    }

    @Override
    public List<GenericCartDTO> getCartsByLimit(int limit) {
        List<GenericCartDTO> allGenericCartDtos = getAllCarts();

        List<GenericCartDTO> cartsInLimit = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            cartsInLimit.add(allGenericCartDtos.get(i));
        }

        return cartsInLimit;
    }

    @Override
    public List<GenericCartDTO> getCartsBySort(String sortType) {
        String sort = "asc";

        if (sortType.equalsIgnoreCase("descending") || sortType.equalsIgnoreCase("desc")) {
            sort = "desc";
        }

        List<Cart> carts;
        if (sort.equals("desc")) {
            carts = cartRepository.findAllByOrderByUuidDesc();
        }
        else {
            carts = cartRepository.findAll();
        }
        List<GenericCartDTO> genericCartDtosList = new ArrayList<>();
        for (Cart cart : carts) {
            genericCartDtosList.add(convertCartToGenericCartDto(cart));
        }
        return genericCartDtosList;
    }

    @Override
    public List<GenericCartDTO> getCartsBySortAndLimit(String sortType, int limit) {
        String sort = "asc";

        if (sortType.equalsIgnoreCase("descending") || sortType.equalsIgnoreCase("desc")) {
            sort = "desc";
        }

        List<Cart> carts;
        if (sort.equals("desc")) {
            carts = cartRepository.findAllByOrderByUuidDesc();
        }
        else {
            carts = cartRepository.findAll();
        }
        List<GenericCartDTO> genericCartDtosList = new ArrayList<>();
        for (Cart cart : carts) {
            genericCartDtosList.add(convertCartToGenericCartDto(cart));
        }

        List<GenericCartDTO> limitedGenericCartsList = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            limitedGenericCartsList.add(genericCartDtosList.get(i));
        }
        return limitedGenericCartsList;
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
