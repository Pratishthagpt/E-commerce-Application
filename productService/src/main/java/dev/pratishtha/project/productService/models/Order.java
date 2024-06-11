package dev.pratishtha.project.productService.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Order extends BaseModel{

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable (
            name = "products_orders",
            joinColumns = @JoinColumn(name = "orderWithId"),
            inverseJoinColumns = @JoinColumn(name = "productWithId")
    )
    private List<Product> products;

}
