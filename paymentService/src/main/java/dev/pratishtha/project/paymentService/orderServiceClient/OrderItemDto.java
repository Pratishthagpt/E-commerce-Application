package dev.pratishtha.project.paymentService.orderServiceClient;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class OrderItemDto {

    private String id;
    private String productId;
    private int quantity;
    private Date addedOn;
    private int price;
}
