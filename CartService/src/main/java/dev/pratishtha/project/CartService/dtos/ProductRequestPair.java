package dev.pratishtha.project.CartService.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestPair {

    private String productId;
    private int quantity;
}
