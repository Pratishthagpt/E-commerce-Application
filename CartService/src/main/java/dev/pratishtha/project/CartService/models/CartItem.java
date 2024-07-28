package dev.pratishtha.project.CartService.models;

import jakarta.persistence.*;
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

    @Temporal(TemporalType.TIMESTAMP)
    private Date itemAddedAt;
}
