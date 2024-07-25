package dev.pratishtha.project.CartService.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenericCartDTO {

    private String cartId;
    private String userId;
    private int totalPrice;
    private int totalItems;

    private List<GenericCartItemDTO> cartItems;
    private Date createdAt;
}
