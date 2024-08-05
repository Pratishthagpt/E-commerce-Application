package dev.pratishtha.project.orderService.controllers;

import dev.pratishtha.project.orderService.dtos.OrderDTO;
import dev.pratishtha.project.orderService.dtos.OrderStatusRequestDTO;
import dev.pratishtha.project.orderService.dtos.ProductByOrderRequestDTO;
import dev.pratishtha.project.orderService.products.ProductDto;
import dev.pratishtha.project.orderService.services.OrderService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders (
            @RequestHeader (HttpHeaders.AUTHORIZATION) String token) {

        List<OrderDTO> orderDTOS = orderService.getAllOrders(token);

        return new ResponseEntity<>(orderDTOS, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createNewOrder (
            @RequestHeader (HttpHeaders.AUTHORIZATION) String token,
            @RequestBody OrderDTO orderRequestDto) {

        OrderDTO orderDto = orderService.createNewOrder(token, orderRequestDto);

        return new ResponseEntity<>(orderDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById (
            @RequestHeader (HttpHeaders.AUTHORIZATION) String token,
            @PathVariable("id") String orderId) {

        OrderDTO orderDto = orderService.getOrderById(token, orderId);

        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<OrderDTO>> getAllOrdersByUser (
            @RequestHeader (HttpHeaders.AUTHORIZATION) String token) {

        List<OrderDTO> orderDTOS = orderService.getAllOrdersByUser(token);

        return new ResponseEntity<>(orderDTOS, HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<OrderDTO> getSingleOrderByUser(
            @RequestHeader (HttpHeaders.AUTHORIZATION) String token,
            @PathVariable("id") String orderId) {

        OrderDTO orderDTO = orderService.getSingleOrderByUser(token, orderId);

        return new ResponseEntity<>(orderDTO, HttpStatus.OK);
    }

    @GetMapping("/user/product")
    public ResponseEntity<ProductDto> getProductByOrderForUser (
            @RequestHeader (HttpHeaders.AUTHORIZATION) String token,
            @RequestBody ProductByOrderRequestDTO requestDTO) {

        ProductDto productDto = orderService.getProductByOrderForUser(token, requestDTO);

        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrderById (
            @RequestHeader (HttpHeaders.AUTHORIZATION) String token,
            @RequestBody OrderDTO orderRequestDto,
            @PathVariable("id") String orderId) {

        OrderDTO orderDto = orderService.updateOrderById(token, orderRequestDto, orderId);

        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @PutMapping("/{id}/order_status")
    public ResponseEntity<OrderDTO> updateOrderStatusByOrderId (
            @RequestHeader (HttpHeaders.AUTHORIZATION) String token,
            @RequestBody OrderStatusRequestDTO requestDTO,
            @PathVariable("id") String orderId) {

        OrderDTO orderDto = orderService.updateOrderStatusByOrderId(token, requestDTO, orderId);

        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderDTO> cancelOrderByOrderId (
            @RequestHeader (HttpHeaders.AUTHORIZATION) String token,
            @PathVariable("id") String orderId) {

        OrderDTO orderDto = orderService.cancelOrderByOrderId(token, orderId);

        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<OrderDTO> deleteOrderById (
            @RequestHeader (HttpHeaders.AUTHORIZATION) String token,
            @PathVariable("id") String orderId) {

        OrderDTO orderDto = orderService.deleteOrderById(token, orderId);

        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }
}
