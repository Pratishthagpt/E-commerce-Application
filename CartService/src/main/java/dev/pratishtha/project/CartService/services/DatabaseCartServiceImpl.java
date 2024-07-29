package dev.pratishtha.project.CartService.services;

import dev.pratishtha.project.CartService.dtos.DateRangeDTO;
import dev.pratishtha.project.CartService.dtos.GenericCartDTO;
import dev.pratishtha.project.CartService.dtos.GenericCartItemDTO;
import dev.pratishtha.project.CartService.exceptions.CartIdNotFoundException;
import dev.pratishtha.project.CartService.exceptions.CartNotPresentException;
import dev.pratishtha.project.CartService.exceptions.InvalidParameterException;
import dev.pratishtha.project.CartService.exceptions.InvalidUserAuthenticationException;
import dev.pratishtha.project.CartService.models.Cart;
import dev.pratishtha.project.CartService.models.CartItem;
import dev.pratishtha.project.CartService.repositories.CartItemRepository;
import dev.pratishtha.project.CartService.repositories.CartRepository;
import dev.pratishtha.project.CartService.security.JwtData;
import dev.pratishtha.project.CartService.security.TokenValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class DatabaseCartServiceImpl implements CartService {

    private CartRepository cartRepository;
    private CartItemRepository cartItemRepository;
    private TokenValidator tokenValidator;

    @Autowired
    public DatabaseCartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository,
                                   TokenValidator tokenValidator) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.tokenValidator = tokenValidator;
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
    public GenericCartDTO addNewCart(String token, GenericCartDTO requestDto) {

//        1. First, we will authenticate the user using TokenValidator class
//        2. Enter the user details into cart

        JwtData userJwtData = validateUserByToken(token);

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
            cartItem.setCart(cart);

            cartItems.add(cartItem);

            totalPrice += (cartItem.getPrice() * cartItem.getQuantity());
        }

        cart.setCartItems(cartItems);

//        setting user Id from user service
        cart.setUserId(userJwtData.getUserId());
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
    public List<GenericCartDTO> getCartsInDateRange(DateRangeDTO dateRangeDTO) {

        LocalDate sDate = dateRangeDTO.getStartDate();
        LocalDate eDate = dateRangeDTO.getEndDate();

//        check for start-date and end-date, if they are null, then set their value
        if (sDate == null) {
            sDate = LocalDate.of(1970, 01, 01);
        }
        if (eDate == null) {
            eDate = LocalDate.now();
        }

//        if start date is greater than end date, then it is invalid input
        if (sDate.compareTo(eDate) > 0) {
            throw new InvalidParameterException("Invalid input date range.");
        }

//        convert start-date and end-date from LocalDate to Date datatype bcoz "createdAt" has a datatype of Date
        Date startDate = Date.from(sDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(eDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

//        finding the list of carts by using custom query from db
        List<Cart> carts = cartRepository.findAllCartsInDateRange(startDate, endDate);

//        System.out.println(carts.size());

        if (carts.size() == 0) {
            throw new CartNotPresentException("There are no carts in this date range.");
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
//            carts = cartRepository.findAllByOrderByUuidDesc();
            carts = cartRepository.findAllByOrderByUuidDescWithLimit(limit);
        }
        else {
//            carts = cartRepository.findAll();
            carts = cartRepository.findAllByOrderByUuidAscWithLimit(limit);
        }
        List<GenericCartDTO> genericCartDtosList = new ArrayList<>();
        for (Cart cart : carts) {
            genericCartDtosList.add(convertCartToGenericCartDto(cart));
        }

//        List<GenericCartDTO> limitedGenericCartsList = new ArrayList<>();
//        for (int i = 0; i < limit; i++) {
//            limitedGenericCartsList.add(genericCartDtosList.get(i));
//        }
        return genericCartDtosList;
    }

    @Override
    public List<GenericCartDTO> getSortedCartsInDateRangeWithLimit(DateRangeDTO dateRangeDTO, int limit, String sortType) {

        LocalDate sDate = dateRangeDTO.getStartDate();
        LocalDate eDate = dateRangeDTO.getEndDate();

        if (sDate == null) {
            sDate = LocalDate.of(1970, 01, 01);
        }
        if (eDate == null) {
            eDate = LocalDate.now();
        }

//        if start date is greater than end date, then it is invalid input
        if (sDate.compareTo(eDate) > 0) {
            throw new InvalidParameterException("Invalid input date range.");
        }

        String sort = "asc";

        if (sortType.equalsIgnoreCase("descending") || sortType.equalsIgnoreCase("desc")) {
            sort = "desc";
        }

//        convert start-date and end-date from LocalDate to Date datatype bcoz "createdAt" has a datatype of Date
        Date startDate = Date.from(sDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(eDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<Cart> carts;
        //        finding the list of carts by using custom query from db
        if(sort.equals("desc")) {
            carts = cartRepository.findAllSortedCartsInDateRangeWithinLimitByDescOrder(startDate, endDate, limit);
        }
        else {
            carts = cartRepository.findAllSortedCartsInDateRangeWithinLimitByAscOrder(startDate, endDate, limit);
        }

        List<GenericCartDTO> genericCartDtosList = new ArrayList<>();
        for (Cart cart : carts) {
            genericCartDtosList.add(convertCartToGenericCartDto(cart));
        }

        return genericCartDtosList;
    }

    @Override
    public List<GenericCartDTO> getCartsByUser(String userId) {
        List<Cart> carts = cartRepository.findAllByUserId(userId);

        if (carts.size() == 0) {
            throw new CartNotPresentException("The user with id - " + userId + " does not have any cart.");
        }

        List<GenericCartDTO> genericCartDtosList = new ArrayList<>();
        for (Cart cart : carts) {
            genericCartDtosList.add(convertCartToGenericCartDto(cart));
        }

        return genericCartDtosList;
    }

    @Override
    public List<GenericCartDTO> getCartsByUserInDateRange(String userId, DateRangeDTO dateRangeDTO) {

        LocalDate sDate = dateRangeDTO.getStartDate();
        LocalDate eDate = dateRangeDTO.getEndDate();

//        check for start-date and end-date, if they are null, then set their value
        if (sDate == null) {
            sDate = LocalDate.of(1970, 01, 01);
        }
        if (eDate == null) {
            eDate = LocalDate.now();
        }

//        if start date is greater than end date, then it is invalid input
        if (sDate.compareTo(eDate) > 0) {
            throw new InvalidParameterException("Invalid input date range.");
        }

//        convert start-date and end-date from LocalDate to Date datatype bcoz "createdAt" has a datatype of Date
        Date startDate = Date.from(sDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(eDate.atStartOfDay(ZoneId.systemDefault()).toInstant());


        List<Cart> carts = cartRepository.findAllByUserIdInDateRange(userId, startDate, endDate);

        if (carts.size() == 0) {
            throw new CartNotPresentException("The user with id - " + userId + " does not have any cart.");
        }

        List<GenericCartDTO> genericCartDtosList = new ArrayList<>();
        for (Cart cart : carts) {
            genericCartDtosList.add(convertCartToGenericCartDto(cart));
        }

        return genericCartDtosList;
    }

    @Override
    public GenericCartDTO updateCartById(String cartId, GenericCartDTO requestDto) {
        UUID id = UUID.fromString(cartId);

        Optional<Cart> cartOptional = cartRepository.findById(id);
        if (cartOptional.isEmpty()) {
            throw new CartIdNotFoundException("Cart with id - " + id + " not found.");
        }

        Cart cart = cartOptional.get();

//      first delete all cart items and removing them from cart item repository as well, bcoz
//        we are not deleting actual products, but we are deleting cartItem.
//        Cart Item (which is not product to be specific, cart item is an item which has
//        its price, product id, quantity etc.) exists only bcoz it is in someone cart.
//        So, we will delete all the cart item and clear the cart and then add the cartItems
//        as per the updated request

        // First, explicitly remove CartItem entities from the cart and delete them from the repository
        // Explicitly remove CartItem entities from the cart and delete them from the repository
        List<CartItem> cartItems = cart.getCartItems();
        for (CartItem cartItem : cartItems) {
            cartItem.setCart(null);  // Detach the cart reference to avoid constraint issues
        }
        cartItemRepository.deleteAllInBatch(cartItems);  // Delete all cart items in batch
        cartItemRepository.flush();  // Ensure the deletions are flushed to the database

        cart.getCartItems().clear(); // Clear the cart's items list

        int totalPrice = 0;
        List<CartItem> newCartItems = new ArrayList<>();

        for (GenericCartItemDTO cartItemDTO : requestDto.getCartItems()) {
            CartItem updatedCartItem = convertGenericCartItemDtoToCartItem(cartItemDTO);
            updatedCartItem.setCart(cart);

            newCartItems.add(updatedCartItem);

            totalPrice += updatedCartItem.getQuantity() * updatedCartItem.getPrice();
        }

        cart.setCartItems(newCartItems);
        cart.setUserId(requestDto.getUserId());
        cart.setCreatedAt(new Date());
        cart.setTotalItems(newCartItems.size());
        cart.setTotalPrice(totalPrice);

        Cart savedCart = cartRepository.save(cart);

        GenericCartDTO genericCartDTO = convertCartToGenericCartDto(savedCart);
        return genericCartDTO;
    }

    @Override
    public GenericCartDTO updateSubCartById(String cartId, GenericCartDTO requestDto) {
        UUID id = UUID.fromString(cartId);

        Optional<Cart> cartOptional = cartRepository.findById(id);
        if (cartOptional.isEmpty()) {
            throw new CartIdNotFoundException("Cart with id - " + id + " not found.");
        }

        Cart cart = cartOptional.get();

        // First, explicitly remove CartItem entities from the cart and delete them from the repository
        // Explicitly remove CartItem entities from the cart and delete them from the repository
        List<CartItem> cartItems = cart.getCartItems();
        for (CartItem cartItem : cartItems) {
            cartItem.setCart(null);  // Detach the cart reference to avoid constraint issues
        }
        cartItemRepository.deleteAllInBatch(cartItems);  // Delete all cart items in batch
        cartItemRepository.flush();  // Ensure the deletions are flushed to the database

        cart.getCartItems().clear(); // Clear the cart's items list

        int totalPrice = 0;
        List<CartItem> newCartItems = new ArrayList<>();

        for (GenericCartItemDTO cartItemDTO : requestDto.getCartItems()) {
            CartItem updatedCartItem = convertGenericCartItemDtoToCartItem(cartItemDTO);
            updatedCartItem.setCart(cart);

            newCartItems.add(updatedCartItem);

            totalPrice += updatedCartItem.getQuantity() * updatedCartItem.getPrice();
        }

        cart.setCartItems(newCartItems);
        cart.setUserId(requestDto.getUserId());
        cart.setCreatedAt(new Date());
        cart.setTotalItems(newCartItems.size());
        cart.setTotalPrice(totalPrice);

        Cart savedCart = cartRepository.save(cart);

        GenericCartDTO genericCartDTO = convertCartToGenericCartDto(savedCart);
        return genericCartDTO;
    }

    @Override
    public GenericCartDTO deleteCartById(String cartId) {
        UUID id = UUID.fromString(cartId);

        Optional<Cart> cartOptional = cartRepository.findById(id);
        if (cartOptional.isEmpty()) {
            throw new CartIdNotFoundException("Cart with id - " + id + " not found.");
        }

        Cart cart = cartOptional.get();

        GenericCartDTO genericCartDTO = convertCartToGenericCartDto(cart);

        cartRepository.deleteById(id);

        return genericCartDTO;
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

    private CartItem convertGenericCartItemDtoToCartItem(GenericCartItemDTO genericCartItemDTO) {
        CartItem cartItem = new CartItem();

        cartItem.setItemAddedAt(genericCartItemDTO.getItemAddedAt());
        cartItem.setPrice(genericCartItemDTO.getPrice());
        cartItem.setQuantity(genericCartItemDTO.getQuantity());
        cartItem.setProductId(genericCartItemDTO.getProductId());

        return cartItem;
    }


    private Cart convertGenericCartDtoToCart(GenericCartDTO genericCartDTO) {
        Cart cart = new Cart();

        cart.setUuid(UUID.fromString(genericCartDTO.getCartId()));
        cart.setUserId(genericCartDTO.getUserId());
        cart.setTotalItems(genericCartDTO.getTotalItems());
        cart.setTotalPrice(genericCartDTO.getTotalPrice());
        cart.setCreatedAt(genericCartDTO.getCreatedAt());

        List<CartItem> cartItems = new ArrayList<>();
        for (GenericCartItemDTO cartItemDTO : genericCartDTO.getCartItems()) {
            cartItems.add(convertGenericCartItemDtoToCartItem(cartItemDTO));
        }

        cart.setCartItems(cartItems);

        return cart;
    }

    public JwtData validateUserByToken (String token) {
        Optional<JwtData> userData = tokenValidator.validateToken(token);
        if (userData.isEmpty()) {
            throw new InvalidUserAuthenticationException("User token is not Authenticated. Please enter the valid authentication token.");
        }

        JwtData userJwtData = userData.get();

        return userJwtData;
    }
}
