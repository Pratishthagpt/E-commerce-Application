package dev.pratishtha.project.CartService.products;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private String id;

    private String title;
    private String description;
    private String image;

    private String category;

    private double priceVal;

    private int inventoryCount;

}
