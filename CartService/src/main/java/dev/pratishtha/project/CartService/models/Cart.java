package dev.pratishtha.project.CartService.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
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

    @OneToMany(cascade = CascadeType.REMOVE)
    private List<CartItem> cartItems;
    private Date createdAt;
}
