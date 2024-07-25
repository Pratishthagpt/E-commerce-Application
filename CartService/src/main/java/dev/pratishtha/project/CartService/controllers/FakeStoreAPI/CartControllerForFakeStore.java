package dev.pratishtha.project.CartService.controllers.FakeStoreAPI;

import dev.pratishtha.project.CartService.dtos.GenericCartDTO;
import dev.pratishtha.project.CartService.services.CartService;
import dev.pratishtha.project.CartService.thirdPartyClients.fakeStore.dtos.FakeStoreCartDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/fakestore/carts")
public class CartControllerForFakeStore {

    private static final Logger LOGGER = Logger.getLogger(CartControllerForFakeStore.class.getName());
    private CartService cartService;

    @Autowired
    public CartControllerForFakeStore(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<List<GenericCartDTO>> getAllCarts () {
//        LOGGER.info("Received request to get all carts");
        List<GenericCartDTO> cartsList = cartService.getAllCarts();

//        LOGGER.info("Returning " + cartsList.size() + " carts");
        return new ResponseEntity<>(cartsList, HttpStatus.OK);
    }

}
