package dev.pratishtha.project.CartService.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CartItem extends BaseModel{

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "cart")
    private Cart cart;

    private String productId;

    private int quantity;
    private int price;
}
