package dev.pratishtha.project.orderService.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class OrderItemDTO {

    private String id;
    private String productId;
    private int quantity;
    private Date createdOn;
}
