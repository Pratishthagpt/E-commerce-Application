package dev.pratishtha.project.productService.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Category extends BaseModel{

    private String name;
    private List<Product> products;

}
