package dev.pratishtha.project.orderService.controllers;


import dev.pratishtha.project.orderService.dtos.AddressDTO;
import dev.pratishtha.project.orderService.services.AddressService;
import dev.pratishtha.project.orderService.services.AddressServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders/addresses")
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

    @PostMapping
    public ResponseEntity<AddressDTO> addNewAddress(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody AddressDTO requestDto) {

        AddressDTO addressDTO = addressService.addNewAddress(token, requestDto);

        return new ResponseEntity<>(addressDTO, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<AddressDTO> getAddressById(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @PathVariable ("id") String id) {

        AddressDTO addressDTO = addressService.getAddressById(token, id);

        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }
}
