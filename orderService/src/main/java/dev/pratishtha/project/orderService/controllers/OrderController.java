package dev.pratishtha.project.orderService.controllers;

import dev.pratishtha.project.orderService.dtos.OrderDTO;
import dev.pratishtha.project.orderService.security.TokenValidator;
import dev.pratishtha.project.orderService.services.OrderService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
