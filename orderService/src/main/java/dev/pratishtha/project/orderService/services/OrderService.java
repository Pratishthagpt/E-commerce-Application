package dev.pratishtha.project.orderService.services;

import dev.pratishtha.project.orderService.dtos.OrderDTO;
import dev.pratishtha.project.orderService.dtos.OrderStatusRequestDTO;
import dev.pratishtha.project.orderService.dtos.ProductByOrderRequestDTO;
import dev.pratishtha.project.orderService.productServiceClient.ProductDto;
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

    OrderDTO updateOrderStatusByOrderId(String token, OrderStatusRequestDTO requestDTO, String orderId);

    OrderDTO cancelOrderByOrderId(String token, String orderId);

    OrderDTO deleteOrderById(String token, String orderId);

    ProductDto getProductByOrderForUser(String token, ProductByOrderRequestDTO requestDTO);
}
