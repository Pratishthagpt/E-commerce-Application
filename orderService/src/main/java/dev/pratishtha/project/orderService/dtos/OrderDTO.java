package dev.pratishtha.project.orderService.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class OrderDTO {

    private String id;
    private String description;
    private String addressId;
    private String orderStatus;
    private String paymentId;
    private Date createdOn;
    private int totalPrice;
    private int quantity;
    private String userId;

    private List<OrderItemDTO> orderItems;
}
