package dev.pratishtha.project.paymentService.orderServiceClient;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class OrderResponseDto {

    private String id;
    private String description;
    private String addressId;
    private String orderStatus;
    private String paymentId;
    private Date createdOn;
    private int totalPrice;
    private int quantity;
    private String userId;

    private List<OrderItemDto> orderItems;
}
