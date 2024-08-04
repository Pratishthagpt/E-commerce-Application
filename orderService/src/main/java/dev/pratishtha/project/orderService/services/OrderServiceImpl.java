package dev.pratishtha.project.orderService.services;

import dev.pratishtha.project.orderService.dtos.AddressDTO;
import dev.pratishtha.project.orderService.dtos.OrderDTO;
import dev.pratishtha.project.orderService.dtos.OrderItemDTO;
import dev.pratishtha.project.orderService.dtos.OrderStatusRequestDTO;
import dev.pratishtha.project.orderService.exceptions.AddressIdNotFoundException;
import dev.pratishtha.project.orderService.exceptions.InvalidUserAuthenticationException;
import dev.pratishtha.project.orderService.exceptions.OrderNotFoundException;
import dev.pratishtha.project.orderService.exceptions.UnAuthorizedUserAccessException;
import dev.pratishtha.project.orderService.models.Address;
import dev.pratishtha.project.orderService.models.Order;
import dev.pratishtha.project.orderService.models.OrderItem;
import dev.pratishtha.project.orderService.models.OrderStatus;
import dev.pratishtha.project.orderService.repositories.AddressRepository;
import dev.pratishtha.project.orderService.repositories.OrderItemRepository;
import dev.pratishtha.project.orderService.repositories.OrderRepository;
import dev.pratishtha.project.orderService.security.JwtData;
import dev.pratishtha.project.orderService.security.TokenValidator;
import dev.pratishtha.project.orderService.security.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService{

    private OrderRepository orderRepository;
    private TokenValidator tokenValidator;
    private AddressRepository addressRepository;
    private OrderItemRepository orderItemRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, TokenValidator tokenValidator,
                            AddressRepository addressRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.tokenValidator = tokenValidator;
        this.addressRepository = addressRepository;
        this.orderRepository = orderRepository;
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

    @Override
    public OrderDTO createNewOrder(String token, OrderDTO orderRequestDto) {

        JwtData userData = validateUserByToken(token);
        String userId = userData.getUserId();

        System.out.println(userId);
        Order order = new Order();

        List<OrderItemDTO> orderItemDTOSRequest = orderRequestDto.getOrderItems();

        List<OrderItem> orderItems = new ArrayList<>();
        int totalPrice = 0;

        for (OrderItemDTO itemDTO : orderItemDTOSRequest) {
            OrderItem orderItem = new OrderItem();

            orderItem.setProductId(itemDTO.getProductId());
            orderItem.setAddedOn(itemDTO.getAddedOn());
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setPrice(itemDTO.getPrice());
            orderItem.setOrder(order);

            orderItems.add(orderItem);

            totalPrice += (orderItem.getQuantity() * orderItem.getPrice());
        }

        order.setUserId(userId);
        order.setOrderItems(orderItems);
        order.setDescription(orderRequestDto.getDescription());
        order.setCreatedOn(new Date());
        order.setTotalPrice(totalPrice);
        order.setOrderStatus(OrderStatus.valueOf(orderRequestDto.getOrderStatus()));
        order.setQuantity(orderItems.size());
        order.setPaymentId(orderRequestDto.getPaymentId());

        Optional<Address> addressOptional = addressRepository.findById(UUID.fromString(orderRequestDto.getAddressId()));
        if (addressOptional.isEmpty()) {
            throw new AddressIdNotFoundException("Address with id - " + orderRequestDto.getAddressId() + " not found.");
        }
        Address address = addressOptional.get();

        order.setAddress(address);

        Order savedOrder = orderRepository.save(order);

        OrderDTO orderDTO = convertOrderToOrderDTO(savedOrder);

        return orderDTO;
    }

    @Override
    public OrderDTO getOrderById(String token, String orderId) {
        JwtData userData = validateUserByToken(token);
        String userId = userData.getUserId();

        List<UserRole> userRoles = userData.getRoles();

//        Only user with role of "ADMIN" can see any order by id
        for (UserRole role : userRoles) {
            if (role.getRole().equalsIgnoreCase("ADMIN")) {
                Optional<Order> orderOptional = orderRepository.findById(UUID.fromString(orderId));

                if (orderOptional.isEmpty()) {
                    throw new OrderNotFoundException("Order with id - " + orderId + " not found.");
                }

                Order order = orderOptional.get();

                OrderDTO orderDTO = convertOrderToOrderDTO(order);
                return orderDTO;
            }
        }
        throw new UnAuthorizedUserAccessException("User is not authorized to access the order.");
    }

    @Override
    public List<OrderDTO> getAllOrdersByUser(String token) {

        JwtData userData = validateUserByToken(token);
        String userId = userData.getUserId();

        List<Order> userOrders = orderRepository.findAllByUserId(userId);

        if (userOrders.size() == 0) {
            throw new OrderNotFoundException("This user has no orders.");
        }

        List<OrderDTO> orderDTOS = new ArrayList<>();

        for (Order order : userOrders) {
            OrderDTO orderDTO = convertOrderToOrderDTO(order);
            orderDTOS.add(orderDTO);
        }
        return orderDTOS;
    }

    @Override
    public OrderDTO getSingleOrderByUser(String token, String orderId) {
        JwtData userData = validateUserByToken(token);
        String userId = userData.getUserId();

        List<Order> userOrders = orderRepository.findAllByUserId(userId);

        if (userOrders.size() == 0) {
            throw new OrderNotFoundException("This user has no orders.");
        }

        for (Order order : userOrders) {
            if (order.getUuid().toString().equals(orderId)) {
                OrderDTO orderDTO = convertOrderToOrderDTO(order);
                return orderDTO;
            }
        }
        throw new OrderNotFoundException("Order with id - " + orderId + " not found.");
    }

    @Override
    public OrderDTO updateOrderById(String token, OrderDTO orderRequestDto, String orderId) {
        JwtData userData = validateUserByToken(token);

        Optional<Order> orderOptional = orderRepository.findById(UUID.fromString(orderId));

        if (orderOptional.isEmpty()) {
            throw new OrderNotFoundException("Order with id - " + orderId + " not found.");
        }

        Order order = orderOptional.get();

        List<OrderItemDTO> orderItemDTOSRequest = orderRequestDto.getOrderItems();

        List<OrderItem> orderItems = new ArrayList<>();
        int totalPrice = 0;

        for (OrderItemDTO itemDTO : orderItemDTOSRequest) {
            OrderItem orderItem = new OrderItem();

            orderItem.setProductId(itemDTO.getProductId());
            orderItem.setAddedOn(itemDTO.getAddedOn());
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setPrice(itemDTO.getPrice());
            orderItem.setOrder(order);

            orderItems.add(orderItem);

            totalPrice += (orderItem.getQuantity() * orderItem.getPrice());
        }

        order.setOrderItems(orderItems);
        order.setDescription(orderRequestDto.getDescription());
        order.setTotalPrice(totalPrice);
        order.setOrderStatus(OrderStatus.CREATED);
        order.setQuantity(orderItems.size());
        order.setPaymentId(orderRequestDto.getPaymentId());

        Optional<Address> addressOptional = addressRepository.findById(UUID.fromString(orderRequestDto.getAddressId()));
        if (addressOptional.isEmpty()) {
            throw new AddressIdNotFoundException("Address with id - " + orderRequestDto.getAddressId() + " not found.");
        }
        Address address = addressOptional.get();

        order.setAddress(address);

        Order savedOrder = orderRepository.save(order);

        OrderDTO orderDTO = convertOrderToOrderDTO(savedOrder);

        return orderDTO;
    }

    @Override
    public OrderDTO updateOrderStatusByOrderId(String token, OrderStatusRequestDTO requestDTO, String orderId) {

//        Only ADMIN can change the order status
        JwtData userData = validateUserByToken(token);
        String userId = userData.getUserId();

        List<UserRole> userRoles = userData.getRoles();

//        Only user with role of "ADMIN" can change order status of any order
        for (UserRole role : userRoles) {
            if (role.getRole().equalsIgnoreCase("ADMIN")) {
                Optional<Order> orderOptional = orderRepository.findById(UUID.fromString(orderId));

                if (orderOptional.isEmpty()) {
                    throw new OrderNotFoundException("Order with id - " + orderId + " not found.");
                }

                Order order = orderOptional.get();

                String orderStatus = requestDTO.getOrderStatus();
                order.setOrderStatus(OrderStatus.valueOf(orderStatus));

                Order updatedOrder = orderRepository.save(order);

                OrderDTO orderDTO = convertOrderToOrderDTO(updatedOrder);
                return orderDTO;
            }
        }
        throw new UnAuthorizedUserAccessException("User is not authorized to change the order status.");

    }

    @Override
    public OrderDTO cancelOrderByOrderId(String token, String orderId) {

//        Any user can cancel the order
        JwtData userData = validateUserByToken(token);

        Optional<Order> orderOptional = orderRepository.findById(UUID.fromString(orderId));

        if (orderOptional.isEmpty()) {
            throw new OrderNotFoundException("Order with id - " + orderId + " not found.");
        }

        Order order = orderOptional.get();

        order.setOrderStatus(OrderStatus.CANCELLED);

        Order cancelledOrder = orderRepository.save(order);

        OrderDTO orderDTO = convertOrderToOrderDTO(cancelledOrder);
        return orderDTO;
    }

    @Override
    public OrderDTO deleteOrderById(String token, String orderId) {

//        Only ADMIN can delete the order
        JwtData userData = validateUserByToken(token);
        String userId = userData.getUserId();

        List<UserRole> userRoles = userData.getRoles();

        for (UserRole role : userRoles) {
            if (role.getRole().equalsIgnoreCase("ADMIN")) {
                Optional<Order> orderOptional = orderRepository.findById(UUID.fromString(orderId));

                if (orderOptional.isEmpty()) {
                    throw new OrderNotFoundException("Order with id - " + orderId + " not found.");
                }

                Order order = orderOptional.get();

                orderRepository.deleteById(order.getUuid());

                OrderDTO orderDTO = convertOrderToOrderDTO(order);
                return orderDTO;
            }
        }
        throw new UnAuthorizedUserAccessException("User is not authorized to delete the order.");
    }

    private OrderDTO convertOrderToOrderDTO(Order order) {

        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setId(String.valueOf(order.getUuid()));
        orderDTO.setOrderStatus(String.valueOf(order.getOrderStatus()));

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
        orderDTO.setUserId(order.getUserId());

        return orderDTO;
    }

    private Order convertOrderDtoToOrder(OrderDTO orderDto) {

        Order order = new Order();

        order.setOrderStatus(OrderStatus.valueOf(orderDto.getOrderStatus()));
        int totalPrice = 0;

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemDTO itemDto : orderDto.getOrderItems()) {
            OrderItem item = convertOrderItemDtoToOrderItem(itemDto);

            totalPrice += itemDto.getPrice();
            orderItems.add(item);
        }

        order.setOrderItems(orderItems);
        order.setDescription(orderDto.getDescription());
        order.setTotalPrice(totalPrice);
        order.setCreatedOn(orderDto.getCreatedOn());
        order.setPaymentId(orderDto.getPaymentId());
        order.setQuantity(orderDto.getQuantity());

        Optional<Address> addressOptional = addressRepository.findById(UUID.fromString(orderDto.getAddressId()));

        if (addressOptional.isEmpty()) {
            throw new AddressIdNotFoundException("Address with id - " + orderDto.getAddressId() + " not found. Please add the address first.");
        }

        Address address = addressOptional.get();

        order.setAddress(address);

        return order;
    }

    private OrderItemDTO convertOrderItemToOrderItemDto(OrderItem orderItem) {

        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setProductId(orderItem.getProductId());
        orderItemDTO.setAddedOn(orderItem.getAddedOn());
        orderItemDTO.setQuantity(orderItem.getQuantity());
        orderItemDTO.setId(String.valueOf(orderItem.getUuid()));
        orderItemDTO.setPrice(orderItem.getPrice());

        return orderItemDTO;
    }

    private OrderItem convertOrderItemDtoToOrderItem(OrderItemDTO itemDto) {

        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(itemDto.getProductId());
        orderItem.setAddedOn(itemDto.getAddedOn());
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
