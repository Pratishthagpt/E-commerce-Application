package dev.pratishtha.project.orderService.controllers;


import dev.pratishtha.project.orderService.dtos.AddressDTO;
import dev.pratishtha.project.orderService.dtos.ExceptionDTO;
import dev.pratishtha.project.orderService.services.AddressService;
import dev.pratishtha.project.orderService.services.AddressServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User Address management API.")
@RestController
@RequestMapping("/api/orders/addresses")
public class AddressController {

    private AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @Operation(summary = "API for getting all addresses.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all addresses.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AddressDTO[].class))
                    }),
            @ApiResponse(responseCode = "401", description = "Invalid token input. Unauthorized user access.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @GetMapping
    public ResponseEntity<List<AddressDTO>> getAllAddresses(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {

        List<AddressDTO> addressDTOs = addressService.getAllAddresses(token);

        return new ResponseEntity<>(addressDTOs, HttpStatus.OK);
    }

    @Operation(summary = "API for adding new address.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Add new address.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AddressDTO.class))
                    }),
            @ApiResponse(responseCode = "401", description = "Invalid token input. Unauthorized user access.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @PostMapping
    public ResponseEntity<AddressDTO> addNewAddress(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody AddressDTO requestDto) {

        AddressDTO addressDTO = addressService.addNewAddress(token, requestDto);

        return new ResponseEntity<>(addressDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "API for getting address by user and id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get address by user and id.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AddressDTO.class))
                    }),
            @ApiResponse(responseCode = "401", description = "Invalid token input. Unauthorized user access.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Address not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @GetMapping("{id}")
    public ResponseEntity<AddressDTO> getAddressById(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @PathVariable ("id") String id) {

        AddressDTO addressDTO = addressService.getAddressById(token, id);

        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }

    @Operation(summary = "API for getting all addresses by user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all addresses by user.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AddressDTO[].class))
                    }),
            @ApiResponse(responseCode = "401", description = "Invalid token input. Unauthorized user access.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Address not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @GetMapping("/user")
    public ResponseEntity<List<AddressDTO>> getAddressByUser(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {

        List<AddressDTO> addressDTOS = addressService.getAddressByUser(token);

        return new ResponseEntity<>(addressDTOS, HttpStatus.OK);
    }

    @Operation(summary = "API for updating address of user and id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update address of user and id.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AddressDTO.class))
                    }),
            @ApiResponse(responseCode = "401", description = "Invalid token input. Unauthorized user access.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Address not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @PutMapping("/user/update")
    public ResponseEntity<AddressDTO> updateAddressByUser(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody AddressDTO requestDto) {

        AddressDTO addressDTO = addressService.updateAddressByUser(token, requestDto);

        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }

    @Operation(summary = "API for deleting address of user and id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete address of user and id.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AddressDTO.class))
                    }),
            @ApiResponse(responseCode = "401", description = "Invalid token input. Unauthorized user access.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Address not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @DeleteMapping("/user/address/{id}")
    public ResponseEntity<AddressDTO> deleteAddressByUser(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @PathVariable ("id") String addressId) {

        AddressDTO addressDTO = addressService.deleteAddressByUser(token, addressId);

        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }

    @Operation(summary = "API for deleting address of user and id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete address of user and id.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AddressDTO.class))
                    }),
            @ApiResponse(responseCode = "401", description = "Invalid token input. Unauthorized user access.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Address not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<AddressDTO> deleteAddressById(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @PathVariable ("id") String addressId) {

        AddressDTO addressDTO = addressService.deleteAddressById(token, addressId);

        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }

}
