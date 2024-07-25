package dev.pratishtha.project.CartService.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Cart extends BaseModel{

    private String userId;
    private int totalPrice;
    private int totalItems;

    @OneToMany(mappedBy = "cart")
    private List<CartItem> cartItems;
}
