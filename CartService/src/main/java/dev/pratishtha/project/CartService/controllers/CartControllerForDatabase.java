package dev.pratishtha.project.CartService.controllers;

import dev.pratishtha.project.CartService.dtos.DateRangeDTO;
import dev.pratishtha.project.CartService.dtos.GenericCartDTO;
import dev.pratishtha.project.CartService.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/database/carts")
public class CartControllerForDatabase {

    private CartService cartService;

    @Autowired
    public CartControllerForDatabase(@Qualifier("databaseCartServiceImpl") CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<List<GenericCartDTO>> getAllCarts () {

        List<GenericCartDTO> cartsList = cartService.getAllCarts();
        return new ResponseEntity<>(cartsList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<GenericCartDTO> addNewCart(@RequestBody GenericCartDTO requestDto){
        GenericCartDTO genericCartDTO = cartService.addNewCart(requestDto);

        return new ResponseEntity<>(genericCartDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericCartDTO> getSingleCartById (@PathVariable ("id") String cartId) {
        GenericCartDTO genericCartDTO = cartService.getCartById(cartId);

        return new ResponseEntity<>(genericCartDTO, HttpStatus.OK);
    }

    @GetMapping("/limit/{limit}")
    public ResponseEntity<List<GenericCartDTO>> getCartsByLimit (@PathVariable ("limit") int limit) {
        List<GenericCartDTO> cartsList = cartService.getCartsByLimit(limit);

        return new ResponseEntity<>(cartsList, HttpStatus.OK);
    }

    @GetMapping("/sort/{sortType}")
    public ResponseEntity<List<GenericCartDTO>> getCartsBySort (@PathVariable ("sortType") String sortType) {
        List<GenericCartDTO> cartsList = cartService.getCartsBySort(sortType);

        return new ResponseEntity<>(cartsList, HttpStatus.OK);
    }

    @GetMapping("/sort/{sortType}/limit/{limit}")
    public ResponseEntity<List<GenericCartDTO>> getCartsBySortAndLimit (
            @PathVariable ("sortType") String sortType, @PathVariable ("limit") int limit) {
        List<GenericCartDTO> cartsList = cartService.getCartsBySortAndLimit(sortType, limit);

        return new ResponseEntity<>(cartsList, HttpStatus.OK);
    }

    @PostMapping("/dateRange")
    public ResponseEntity<List<GenericCartDTO>> getCartsInDateRange (
            @RequestBody DateRangeDTO dateRangeDTO) {
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
}
