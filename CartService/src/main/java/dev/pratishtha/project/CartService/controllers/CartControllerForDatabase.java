package dev.pratishtha.project.CartService.controllers;

import dev.pratishtha.project.CartService.dtos.AddProductsRequestDTO;
import dev.pratishtha.project.CartService.dtos.DateRangeDTO;
import dev.pratishtha.project.CartService.dtos.ExceptionDTO;
import dev.pratishtha.project.CartService.dtos.GenericCartDTO;
import dev.pratishtha.project.CartService.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@Tag(name = "Cart management from database.")
@RestController
@RequestMapping("/api/database/carts")
public class CartControllerForDatabase {

    private CartService cartService;

    @Autowired
    public CartControllerForDatabase(@Qualifier("databaseCartServiceImpl") CartService cartService) {
        this.cartService = cartService;
    }

    @Operation(summary = "API for getting all carts.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all carts.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericCartDTO[].class))
                    }),
            @ApiResponse(responseCode = "401", description = "Invalid token input. Unauthorized user access.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @GetMapping
    public ResponseEntity<List<GenericCartDTO>> getAllCarts (
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {

        List<GenericCartDTO> cartsList = cartService.getAllCarts(token);
        return new ResponseEntity<>(cartsList, HttpStatus.OK);
    }

    @Operation(summary = "API for adding new cart.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Add new cart.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericCartDTO.class))
                    }),
            @ApiResponse(responseCode = "401", description = "Invalid token input. Unauthorized user access.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @PostMapping
    public ResponseEntity<GenericCartDTO> addNewCart(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody GenericCartDTO requestDto){

        GenericCartDTO genericCartDTO = cartService.addNewCart(token, requestDto);

        return new ResponseEntity<>(genericCartDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "API for adding products as cart items to cart.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Add products as cart items to cart.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericCartDTO.class))
                    }),
            @ApiResponse(responseCode = "401", description = "Invalid token input. Unauthorized user access.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @PostMapping("/{id}")
    public ResponseEntity<GenericCartDTO> addProductsToCart(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable("id") String cartId,
            @RequestBody AddProductsRequestDTO addProductsRequestDTO){

        GenericCartDTO genericCartDTO = cartService.addProductsToCart(token, cartId, addProductsRequestDTO);

        return new ResponseEntity<>(genericCartDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "API for getting cart by Id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get cart by Id.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericCartDTO.class))
                    }),
            @ApiResponse(responseCode = "401", description = "Invalid token input. Unauthorized user access.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Cart not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @GetMapping("/{id}")
    public ResponseEntity<GenericCartDTO> getSingleCartById (@PathVariable ("id") String cartId) {
        GenericCartDTO genericCartDTO = cartService.getCartById(cartId);

        return new ResponseEntity<>(genericCartDTO, HttpStatus.OK);
    }

    @Operation(summary = "API for getting all carts by limit.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all carts within limit.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericCartDTO[].class))
                    }),
            @ApiResponse(responseCode = "401", description = "Invalid token input. Unauthorized user access.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @GetMapping("/limit/{limit}")
    public ResponseEntity<List<GenericCartDTO>> getCartsByLimit (
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable ("limit") int limit) {
        List<GenericCartDTO> cartsList = cartService.getCartsByLimit(token, limit);

        return new ResponseEntity<>(cartsList, HttpStatus.OK);
    }

    @Operation(summary = "API for getting all carts sorted by Id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all carts sorted by Id.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericCartDTO[].class))
                    }),
            @ApiResponse(responseCode = "401", description = "Invalid token input. Unauthorized user access.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @GetMapping("/sort/{sortType}")
    public ResponseEntity<List<GenericCartDTO>> getCartsBySort (@PathVariable ("sortType") String sortType) {
        List<GenericCartDTO> cartsList = cartService.getCartsBySort(sortType);

        return new ResponseEntity<>(cartsList, HttpStatus.OK);
    }

    @Operation(summary = "API for getting all carts sorted by Id within limit.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all carts sorted by Id within limit.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericCartDTO[].class))
                    }),
            @ApiResponse(responseCode = "401", description = "Invalid token input. Unauthorized user access.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @GetMapping("/sort/{sortType}/limit/{limit}")
    public ResponseEntity<List<GenericCartDTO>> getCartsBySortAndLimit (
            @PathVariable ("sortType") String sortType, @PathVariable ("limit") int limit) {
        List<GenericCartDTO> cartsList = cartService.getCartsBySortAndLimit(sortType, limit);

        return new ResponseEntity<>(cartsList, HttpStatus.OK);
    }

    @Operation(summary = "API for getting all carts created in particular data range.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all carts created in particular data range.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericCartDTO[].class))
                    }),
            @ApiResponse(responseCode = "400", description = "Invalid date input. Bad Request.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Carts not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @PostMapping("/dateRange")
    public ResponseEntity<List<GenericCartDTO>> getCartsInDateRange (
            @RequestBody DateRangeDTO dateRangeDTO) {
        List<GenericCartDTO> cartsList = cartService.getCartsInDateRange(dateRangeDTO);

        return new ResponseEntity<>(cartsList, HttpStatus.OK);
    }

    @Operation(summary = "API for getting all carts sorted by id and created in particular data range within input limit.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all carts sorted by id and created in particular data range within input limit.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericCartDTO[].class))
                    }),
            @ApiResponse(responseCode = "400", description = "Invalid date input. Bad Request.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Carts not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @PostMapping("/dateRange/sort/{sortType}/limit/{limit}")
    public ResponseEntity<List<GenericCartDTO>> getSortedCartsInDateRangeWithLimit
            (@PathVariable ("limit") int limit,
             @PathVariable ("sortType") String sortType,
             @RequestBody DateRangeDTO dateRangeDTO) {

        List<GenericCartDTO> cartsList = cartService.getSortedCartsInDateRangeWithLimit(dateRangeDTO, limit, sortType);

        return new ResponseEntity<>(cartsList, HttpStatus.OK);
    }

    @Operation(summary = "API for getting cart by user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get cart by user.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericCartDTO[].class))
                    }),
            @ApiResponse(responseCode = "401", description = "Invalid token input. Unauthorized user access.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Cart not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @GetMapping("/user")
    public ResponseEntity<List<GenericCartDTO>> getCartsByUserByToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {

        List<GenericCartDTO> cartsList = cartService.getCartsByUserByToken(token);

        return new ResponseEntity<>(cartsList, HttpStatus.OK);
    }

    @Operation(summary = "API for getting cart by user created in date range.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get cart by user created in date range.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericCartDTO[].class))
                    }),
            @ApiResponse(responseCode = "401", description = "Invalid token input. Unauthorized user access.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Invalid date input. Bad Request.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Cart not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @PostMapping ("/user/dateRange")
    public ResponseEntity<List<GenericCartDTO>> getCartsByUserInDateRange(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody DateRangeDTO dateRangeDTO) {

        List<GenericCartDTO> cartsList = cartService.getCartsByUserTokenInDateRange(token, dateRangeDTO);

        return new ResponseEntity<>(cartsList, HttpStatus.OK);
    }

    @Operation(summary = "API for updating cart by user and cart Id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update cart by user and cart Id.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericCartDTO[].class))
                    }),
            @ApiResponse(responseCode = "401", description = "Invalid token input. Unauthorized user access.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Cart not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @PutMapping("/{id}")
    public ResponseEntity<GenericCartDTO> updateCartById(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @PathVariable ("id") String id, @RequestBody GenericCartDTO requestDto){
        GenericCartDTO genericCartDTO = cartService.updateCartById(token, id, requestDto);

        return new ResponseEntity<>(genericCartDTO, HttpStatus.OK);
    }

    @Operation(summary = "API for updating some part of cart by user and cart Id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update some part of cart by user and cart Id.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericCartDTO[].class))
                    }),
            @ApiResponse(responseCode = "401", description = "Invalid token input. Unauthorized user access.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Cart not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @PatchMapping("/{id}")
    public ResponseEntity<GenericCartDTO> updateSubCartById(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @PathVariable ("id") String id, @RequestBody GenericCartDTO requestDto){

        GenericCartDTO genericCartDTO = cartService.updateSubCartById(token, id, requestDto);

        return new ResponseEntity<>(genericCartDTO, HttpStatus.OK);
    }

    @Operation(summary = "API for deleting cart by user and cart Id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete cart by user and cart Id.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericCartDTO[].class))
                    }),
            @ApiResponse(responseCode = "401", description = "Invalid token input. Unauthorized user access.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Cart not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericCartDTO> deleteCartById(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @PathVariable ("id") String id){
        GenericCartDTO genericCartDTO = cartService.deleteCartById(token, id);

        return new ResponseEntity<>(genericCartDTO, HttpStatus.OK);
    }

}
