package dev.pratishtha.project.orderService.models;

import java.util.Date;

public class OrderItem extends BaseModel{

    private String productId;
    private int quantity;
    private Order order;
    private Date createdOn;
}
