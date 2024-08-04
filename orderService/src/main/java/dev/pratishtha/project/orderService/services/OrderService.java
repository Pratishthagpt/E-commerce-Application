package dev.pratishtha.project.orderService.services;

import dev.pratishtha.project.orderService.dtos.OrderDTO;
import dev.pratishtha.project.orderService.models.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {

    public List<OrderDTO> getAllOrders(String token);

    OrderDTO createNewOrder(String token, OrderDTO orderRequestDto);

    OrderDTO getOrderById(String token, String orderId);

    List<OrderDTO> getAllOrdersByUser(String token);

    OrderDTO getSingleOrderByUser(String token, String orderId);

    OrderDTO updateOrderById(String token, OrderDTO orderRequestDto, String orderId);
}
