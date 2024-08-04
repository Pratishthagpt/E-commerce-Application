package dev.pratishtha.project.orderService.controllers;

import dev.pratishtha.project.orderService.dtos.OrderDTO;
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
}
