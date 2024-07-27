package dev.pratishtha.project.CartService.controllers;

import dev.pratishtha.project.CartService.dtos.GenericCartDTO;
import dev.pratishtha.project.CartService.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
