package dev.pratishtha.project.orderService.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductByOrderRequestDTO {

    private String orderId;
    private String productId;
}
