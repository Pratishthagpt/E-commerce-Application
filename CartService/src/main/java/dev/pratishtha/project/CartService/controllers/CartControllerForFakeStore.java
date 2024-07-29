package dev.pratishtha.project.CartService.controllers;

import ch.qos.logback.core.testUtil.RandomUtil;
import dev.pratishtha.project.CartService.dtos.DateRangeDTO;
import dev.pratishtha.project.CartService.dtos.GenericCartDTO;
import dev.pratishtha.project.CartService.services.CartService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fakestore/carts")
public class CartControllerForFakeStore {

//    private static final Logger LOGGER = Logger.getLogger(CartControllerForFakeStore.class.getName());
    private CartService cartService;

//    adding token to match with database controller methods
    private String token = RandomStringUtils.randomAlphabetic(30);

    @Autowired
    public CartControllerForFakeStore(@Qualifier("fakeStoreCartServiceImpl") CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<List<GenericCartDTO>> getAllCarts () {
//        LOGGER.info("Received request to get all carts");
        List<GenericCartDTO> cartsList = cartService.getAllCarts(token);

//        LOGGER.info("Returning " + cartsList.size() + " carts");
        return new ResponseEntity<>(cartsList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericCartDTO> getSingleCartById (@PathVariable ("id") String cartId) {
        GenericCartDTO genericCartDTO = cartService.getCartById(cartId);

        return new ResponseEntity<>(genericCartDTO, HttpStatus.OK);
    }

    @GetMapping("/limit/{limit}")
    public ResponseEntity<List<GenericCartDTO>> getCartsByLimit (@PathVariable ("limit") int limit) {
        List<GenericCartDTO> cartsList = cartService.getCartsByLimit(token, limit);

        return new ResponseEntity<>(cartsList, HttpStatus.OK);
    }

    @GetMapping("/sort/{sortType}")
    public ResponseEntity<List<GenericCartDTO>> getCartsBySort (@PathVariable ("sortType") String sortType) {
        List<GenericCartDTO> cartsList = cartService.getCartsBySort(sortType);

        return new ResponseEntity<>(cartsList, HttpStatus.OK);
    }

    @PostMapping("/dateRange")
    public ResponseEntity<List<GenericCartDTO>> getCartsInDateRange (@RequestBody DateRangeDTO dateRangeDTO) {
        List<GenericCartDTO> cartsList = cartService.getCartsInDateRange(dateRangeDTO);

        return new ResponseEntity<>(cartsList, HttpStatus.OK);
    }

    @PostMapping("/dateRange/sort/{sortType}/limit/{limit}")
    public ResponseEntity<List<GenericCartDTO>> getSortedCartsInDateRangeWithLimit
            (@PathVariable ("limit") int limit,
             @PathVariable ("sortType") String sortType,
             @RequestBody DateRangeDTO dateRangeDTO) {

        List<GenericCartDTO> cartsList = cartService.getSortedCartsInDateRangeWithLimit(dateRangeDTO, limit, sortType);

        return new ResponseEntity<>(cartsList, HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<GenericCartDTO>> getCartsByUser(@PathVariable ("id") String userId) {

        List<GenericCartDTO> cartsList = cartService.getCartsByUser(userId);

        return new ResponseEntity<>(cartsList, HttpStatus.OK);
    }

    @PostMapping ("/user/{id}/dateRange")
    public ResponseEntity<List<GenericCartDTO>> getCartsByUserInDateRange(
            @PathVariable ("id") String userId, @RequestBody DateRangeDTO dateRangeDTO) {

        List<GenericCartDTO> cartsList = cartService.getCartsByUserInDateRange(userId, dateRangeDTO);

        return new ResponseEntity<>(cartsList, HttpStatus.OK);
    }


//    hitting this api to fakestore api won't make any change in actual api carts.
//    We are just returning the cart directly that we are creating.
//     If you send an object like the code above, it will return you an object with a new id.
//     Remember that nothing in real will insert into the database. so if you want to access the new id you will get a 404 error.
    @PostMapping
    public ResponseEntity<GenericCartDTO> addNewCart(@RequestBody GenericCartDTO requestDto){

        GenericCartDTO genericCartDTO = cartService.addNewCart(token, requestDto);

        return new ResponseEntity<>(genericCartDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenericCartDTO> updateCartById(@PathVariable ("id") String id, @RequestBody GenericCartDTO requestDto){
        GenericCartDTO genericCartDTO = cartService.updateCartById(id, requestDto);

        return new ResponseEntity<>(genericCartDTO, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GenericCartDTO> updateSubCartById(@PathVariable ("id") String id, @RequestBody GenericCartDTO requestDto){
        GenericCartDTO genericCartDTO = cartService.updateSubCartById(id, requestDto);

        return new ResponseEntity<>(genericCartDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericCartDTO> deleteCartById(@PathVariable ("id") String id){
        GenericCartDTO genericCartDTO = cartService.deleteCartById(id);

        return new ResponseEntity<>(genericCartDTO, HttpStatus.OK);
    }

}
