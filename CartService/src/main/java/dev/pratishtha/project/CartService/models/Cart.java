package dev.pratishtha.project.CartService.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
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
    private Date createdAt;
}
