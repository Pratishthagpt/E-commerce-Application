package dev.pratishtha.project.CartService.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private String productId;
    private String title;
    private String description;
    private String image;

    private String category;

    private int price;

    private int inventoryCount;
}
