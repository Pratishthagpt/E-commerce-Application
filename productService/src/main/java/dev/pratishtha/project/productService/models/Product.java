package dev.pratishtha.project.productService.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product extends BaseModel{

    private String title;
    private String description;
    private String image;
    private Category category;
    private Price price;

    private int inventoryCount;

}
