package dev.pratishtha.project.orderService.controllers;


import dev.pratishtha.project.orderService.dtos.AddressDTO;
import dev.pratishtha.project.orderService.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/order/user/address")
public class AddressController {

    private AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public ResponseEntity<List<AddressDTO>> getAllAddresses(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {

        List<AddressDTO> addressDTOs = addressService.getAllAddresses(token);

        return new ResponseEntity<>(addressDTOs, HttpStatus.OK);
    }
}
