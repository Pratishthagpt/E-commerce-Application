package dev.pratishtha.project.CartService.models;

import jakarta.persistence.*;
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

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
}
