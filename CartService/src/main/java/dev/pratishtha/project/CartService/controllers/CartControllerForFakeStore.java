package dev.pratishtha.project.CartService.controllers;

import ch.qos.logback.core.testUtil.RandomUtil;
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
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Cart management from FakeStore API.")
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

    @Operation(summary = "API for getting all carts.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all carts.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericCartDTO[].class))
                    })
    })
    @GetMapping
    public ResponseEntity<List<GenericCartDTO>> getAllCarts () {
//        LOGGER.info("Received request to get all carts");
        List<GenericCartDTO> cartsList = cartService.getAllCarts(token);

//        LOGGER.info("Returning " + cartsList.size() + " carts");
        return new ResponseEntity<>(cartsList, HttpStatus.OK);
    }

    @Operation(summary = "API for getting cart by Id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get cart by Id.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericCartDTO.class))
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
                    })
    })
    @GetMapping("/limit/{limit}")
    public ResponseEntity<List<GenericCartDTO>> getCartsByLimit (@PathVariable ("limit") int limit) {
        List<GenericCartDTO> cartsList = cartService.getCartsByLimit(token, limit);

        return new ResponseEntity<>(cartsList, HttpStatus.OK);
    }

    @Operation(summary = "API for getting all carts sorted by Id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all carts sorted by Id.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericCartDTO[].class))
                    })
    })
    @GetMapping("/sort/{sortType}")
    public ResponseEntity<List<GenericCartDTO>> getCartsBySort (@PathVariable ("sortType") String sortType) {
        List<GenericCartDTO> cartsList = cartService.getCartsBySort(sortType);

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
    public ResponseEntity<List<GenericCartDTO>> getCartsInDateRange (@RequestBody DateRangeDTO dateRangeDTO) {
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
            @ApiResponse(responseCode = "404", description = "Cart not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @GetMapping("/user/{id}")
    public ResponseEntity<List<GenericCartDTO>> getCartsByUser(@PathVariable ("id") String userId) {

        List<GenericCartDTO> cartsList = cartService.getCartsByUser(userId);

        return new ResponseEntity<>(cartsList, HttpStatus.OK);
    }

    @Operation(summary = "API for getting cart by user created in date range.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get cart by user created in date range.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericCartDTO[].class))
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
    @Operation(summary = "API for adding new cart.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Add new cart.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericCartDTO.class))
                    })
    })
    @PostMapping
    public ResponseEntity<GenericCartDTO> addNewCart(@RequestBody GenericCartDTO requestDto){

        GenericCartDTO genericCartDTO = cartService.addNewCart(token, requestDto);

        return new ResponseEntity<>(genericCartDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "API for updating cart by user and cart Id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update cart by user and cart Id.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericCartDTO[].class))
                    }),
            @ApiResponse(responseCode = "404", description = "Cart not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @PutMapping("/{id}")
    public ResponseEntity<GenericCartDTO> updateCartById(@PathVariable ("id") String id, @RequestBody GenericCartDTO requestDto){
        GenericCartDTO genericCartDTO = cartService.updateCartById(token, id, requestDto);

        return new ResponseEntity<>(genericCartDTO, HttpStatus.OK);
    }

    @Operation(summary = "API for updating some part of cart by user and cart Id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update some part of cart by user and cart Id.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericCartDTO[].class))
                    }),
            @ApiResponse(responseCode = "404", description = "Cart not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @PatchMapping("/{id}")
    public ResponseEntity<GenericCartDTO> updateSubCartById(@PathVariable ("id") String id, @RequestBody GenericCartDTO requestDto){
        GenericCartDTO genericCartDTO = cartService.updateSubCartById(token, id, requestDto);

        return new ResponseEntity<>(genericCartDTO, HttpStatus.OK);
    }

    @Operation(summary = "API for deleting cart by user and cart Id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete cart by user and cart Id.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericCartDTO[].class))
                    }),
            @ApiResponse(responseCode = "404", description = "Cart not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericCartDTO> deleteCartById(@PathVariable ("id") String id){
        GenericCartDTO genericCartDTO = cartService.deleteCartById(token, id);

        return new ResponseEntity<>(genericCartDTO, HttpStatus.OK);
    }

}
