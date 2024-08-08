package dev.pratishtha.project.orderService.productServiceClient;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {

    private String id;

    private String title;
    private String description;
    private String image;

    private String category;

    private double priceVal;

    private int inventoryCount;
}
