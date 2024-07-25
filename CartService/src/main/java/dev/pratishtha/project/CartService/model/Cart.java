package dev.pratishtha.project.CartService.model;

import java.util.List;
import java.util.UUID;

public class Cart extends BaseModel{

    private User user;
    private int totalPrice;
    private int totalItems;
    private List<CartItem> cartItems;
}
