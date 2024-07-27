package dev.pratishtha.project.CartService.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class CartItem extends BaseModel{

    private String productId;

    private int quantity;
    private int price;
    private Date itemAddedAt;
}
