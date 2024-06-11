package dev.pratishtha.project.productService.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Order extends BaseModel{

    private List<Product> products;

}
