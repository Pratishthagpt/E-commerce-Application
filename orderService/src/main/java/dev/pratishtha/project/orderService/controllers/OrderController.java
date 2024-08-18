package dev.pratishtha.project.orderService.controllers;

import dev.pratishtha.project.orderService.dtos.*;
import dev.pratishtha.project.orderService.productServiceClient.ProductDto;
import dev.pratishtha.project.orderService.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Order management API.")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "API for getting all orders.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all orders.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDTO[].class))
                    }),
            @ApiResponse(responseCode = "401", description = "Invalid token input. Unauthorized user access.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders (
            @RequestHeader (HttpHeaders.AUTHORIZATION) String token) {

        List<OrderDTO> orderDTOS = orderService.getAllOrders(token);

        return new ResponseEntity<>(orderDTOS, HttpStatus.OK);
    }

    @Operation(summary = "API for adding new order.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Add new order.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDTO.class))
                    }),
            @ApiResponse(responseCode = "401", description = "Invalid token input. Unauthorized user access.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Address not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    }),
            @ApiResponse(responseCode = "409", description = "Product out of stock. Insufficient product quantity.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @PostMapping
    public ResponseEntity<OrderDTO> createNewOrder (
            @RequestHeader (HttpHeaders.AUTHORIZATION) String token,
            @RequestBody OrderDTO orderRequestDto) {

        OrderDTO orderDto = orderService.createNewOrder(token, orderRequestDto);

        return new ResponseEntity<>(orderDto, HttpStatus.CREATED);
    }

    @Operation(summary = "API for getting order by admin and order-id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get order by user and order-id.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDTO.class))
                    }),
            @ApiResponse(responseCode = "401", description = "Invalid token input. Unauthorized user access.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Order not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById (
            @RequestHeader (HttpHeaders.AUTHORIZATION) String token,
            @PathVariable("id") String orderId) {

        OrderDTO orderDto = orderService.getOrderById(token, orderId);

        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @Operation(summary = "API for getting all orders by user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all orders by user.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDTO[].class))
                    }),
            @ApiResponse(responseCode = "401", description = "Invalid token input. Unauthorized user access.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Order not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @GetMapping("/user")
    public ResponseEntity<List<OrderDTO>> getAllOrdersByUser (
            @RequestHeader (HttpHeaders.AUTHORIZATION) String token) {

        List<OrderDTO> orderDTOS = orderService.getAllOrdersByUser(token);

        return new ResponseEntity<>(orderDTOS, HttpStatus.OK);
    }

    @Operation(summary = "API for getting order by user and order-id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get order by user and order-id.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDTO.class))
                    }),
            @ApiResponse(responseCode = "401", description = "Invalid token input. Unauthorized user access.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Order not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @GetMapping("/user/{id}")
    public ResponseEntity<OrderDTO> getSingleOrderByUser(
            @RequestHeader (HttpHeaders.AUTHORIZATION) String token,
            @PathVariable("id") String orderId) {

        OrderDTO orderDTO = orderService.getSingleOrderByUser(token, orderId);

        return new ResponseEntity<>(orderDTO, HttpStatus.OK);
    }

    @Operation(summary = "API for getting product from user order by product-id and order-id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get product from user order by product-id and order-id.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDTO.class))
                    }),
            @ApiResponse(responseCode = "401", description = "Invalid token input. Unauthorized user access.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Product / Order not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @GetMapping("/user/product")
    public ResponseEntity<ProductDto> getProductByOrderForUser (
            @RequestHeader (HttpHeaders.AUTHORIZATION) String token,
            @RequestBody ProductByOrderRequestDTO requestDTO) {

        ProductDto productDto = orderService.getProductByOrderForUser(token, requestDTO);

        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @Operation(summary = "API for updating order by order-id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update order by order-id.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDTO.class))
                    }),
            @ApiResponse(responseCode = "401", description = "Invalid token input. Unauthorized user access.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Address / Order not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrderById (
            @RequestHeader (HttpHeaders.AUTHORIZATION) String token,
            @RequestBody OrderDTO orderRequestDto,
            @PathVariable("id") String orderId) {

        OrderDTO orderDto = orderService.updateOrderById(token, orderRequestDto, orderId);

        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @Operation(summary = "API for updating order status for order-id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update order status for order-id.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDTO.class))
                    }),
            @ApiResponse(responseCode = "401", description = "Invalid token input. Unauthorized user access.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Order not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @PutMapping("/{id}/order_status")
    public ResponseEntity<OrderDTO> updateOrderStatusByOrderId (
            @RequestHeader (HttpHeaders.AUTHORIZATION) String token,
            @RequestBody OrderStatusRequestDTO requestDTO,
            @PathVariable("id") String orderId) {

        OrderDTO orderDto = orderService.updateOrderStatusByOrderId(token, requestDTO, orderId);

        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @Operation(summary = "API for cancelling order for order-id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cancel order for order-id.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDTO.class))
                    }),
            @ApiResponse(responseCode = "401", description = "Invalid token input. Unauthorized user access.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Order not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderDTO> cancelOrderByOrderId (
            @RequestHeader (HttpHeaders.AUTHORIZATION) String token,
            @PathVariable("id") String orderId) {

        OrderDTO orderDto = orderService.cancelOrderByOrderId(token, orderId);

        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @Operation(summary = "API for deleting order for order-id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete order for order-id.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDTO.class))
                    }),
            @ApiResponse(responseCode = "401", description = "Invalid token input. Unauthorized user access.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Order not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<OrderDTO> deleteOrderById (
            @RequestHeader (HttpHeaders.AUTHORIZATION) String token,
            @PathVariable("id") String orderId) {

        OrderDTO orderDto = orderService.deleteOrderById(token, orderId);

        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }
}
