package dev.pratishtha.project.orderService.services;

import dev.pratishtha.project.orderService.dtos.AddressDTO;
import dev.pratishtha.project.orderService.dtos.OrderDTO;
import dev.pratishtha.project.orderService.dtos.OrderItemDTO;
import dev.pratishtha.project.orderService.exceptions.AddressIdNotFoundException;
import dev.pratishtha.project.orderService.exceptions.InvalidUserAuthenticationException;
import dev.pratishtha.project.orderService.exceptions.UnAuthorizedUserAccessException;
import dev.pratishtha.project.orderService.models.Address;
import dev.pratishtha.project.orderService.models.Order;
import dev.pratishtha.project.orderService.models.OrderItem;
import dev.pratishtha.project.orderService.models.OrderStatus;
import dev.pratishtha.project.orderService.repositories.AddressRepository;
import dev.pratishtha.project.orderService.repositories.OrderRepository;
import dev.pratishtha.project.orderService.security.JwtData;
import dev.pratishtha.project.orderService.security.TokenValidator;
import dev.pratishtha.project.orderService.security.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService{

    private OrderRepository orderRepository;
    private TokenValidator tokenValidator;
    private AddressRepository addressRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, TokenValidator tokenValidator,
                            AddressRepository addressRepository) {
        this.orderRepository = orderRepository;
        this.tokenValidator = tokenValidator;
        this.addressRepository = addressRepository;
    }

    @Override
    public List<OrderDTO> getAllOrders(String token) {

        JwtData userData = validateUserByToken(token);
        String userId = userData.getUserId();

        List<UserRole> userRoles = userData.getRoles();

//        Only user with role of "ADMIN" can see all orders of all users
        for (UserRole role : userRoles) {
            if (role.getRole().equalsIgnoreCase("ADMIN")) {
                List<Order> orders = orderRepository.findAll();

                List<OrderDTO> orderDTOS = new ArrayList<>();

                for (Order order : orders) {
                    OrderDTO orderDTO = convertOrderToOrderDTO(order);
                    orderDTOS.add(orderDTO);
                }
                return orderDTOS;
            }
        }
        throw new UnAuthorizedUserAccessException("User is not authorized to access all the orders.");
    }

    private OrderDTO convertOrderToOrderDTO(Order order) {

        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setId(String.valueOf(order.getUuid()));
        orderDTO.setOrderStatus(orderDTO.getOrderStatus());

        List<OrderItemDTO> orderItemDTOS = new ArrayList<>();
        for (OrderItem orderItem : order.getOrderItems()) {
            OrderItemDTO itemDTO = convertOrderItemToOrderItemDto(orderItem);

            orderItemDTOS.add(itemDTO);
        }

        orderDTO.setOrderItems(orderItemDTOS);
        orderDTO.setAddressId(String.valueOf(order.getAddress().getUuid()));
        orderDTO.setDescription(order.getDescription());
        orderDTO.setTotalPrice(order.getTotalPrice());
        orderDTO.setCreatedOn(order.getCreatedOn());
        orderDTO.setPaymentId(order.getPaymentId());
        orderDTO.setQuantity(order.getQuantity());

        return orderDTO;
    }

    private Order convertOrderDtoToOrder(OrderDTO orderDto) {

        Order order = new Order();

        order.setOrderStatus(OrderStatus.valueOf(orderDto.getOrderStatus()));

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemDTO itemDto : orderDto.getOrderItems()) {
            OrderItem item = convertOrderItemDtoToOrderItem(itemDto);

            orderItems.add(item);
        }

        order.setOrderItems(orderItems);
        order.setDescription(order.getDescription());
        order.setTotalPrice(order.getTotalPrice());
        order.setCreatedOn(order.getCreatedOn());
        order.setPaymentId(order.getPaymentId());
        order.setQuantity(order.getQuantity());

        Optional<Address> addressOptional = addressRepository.findById(UUID.fromString(orderDto.getAddressId()));

        if (addressOptional.isEmpty()) {
            throw new AddressIdNotFoundException("Address with id - " + orderDto.getAddressId() + " not found.");
        }

        Address address = addressOptional.get();

        order.setAddress(address);

        return order;
    }

    private OrderItemDTO convertOrderItemToOrderItemDto(OrderItem orderItem) {

        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setProductId(orderItem.getProductId());
        orderItemDTO.setCreatedOn(orderItem.getCreatedOn());
        orderItemDTO.setQuantity(orderItem.getQuantity());
        orderItemDTO.setId(String.valueOf(orderItem.getUuid()));

        return orderItemDTO;
    }

    private OrderItem convertOrderItemDtoToOrderItem(OrderItemDTO itemDto) {

        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(itemDto.getProductId());
        orderItem.setCreatedOn(itemDto.getCreatedOn());
        orderItem.setQuantity(itemDto.getQuantity());

        return orderItem;
    }

    private JwtData validateUserByToken(String token) {
        Optional<JwtData> userData = tokenValidator.validateToken(token);
        if (userData.isEmpty()) {
            throw new InvalidUserAuthenticationException("User token is not Authenticated. Please enter the valid authentication token.");
        }

        JwtData userJwtData = userData.get();

        return userJwtData;
    }
}
