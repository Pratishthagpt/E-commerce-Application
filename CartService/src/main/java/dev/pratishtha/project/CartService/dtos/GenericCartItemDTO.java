package dev.pratishtha.project.CartService.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenericCartItemDTO {

    private String cartItemId;
    private String productId;

    private int quantity;
    private int price;
    private Date itemAddedAt;
}
