package dev.pratishtha.project.orderService.models;

import java.util.Date;
import java.util.List;

public class Order extends BaseModel{

    private String description;
    private Address address;
    private OrderStatus orderStatus;
    private String paymentId;
    private Date createdOn;
    private int totalPrice;
    private int quantity;
    private String userId;
    private List<OrderItem> orderItems;

}
