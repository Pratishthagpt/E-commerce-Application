package dev.pratishtha.project.CartService.controllers;

import dev.pratishtha.project.CartService.dtos.GenericCartDTO;
import dev.pratishtha.project.CartService.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
