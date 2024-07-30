package dev.pratishtha.project.CartService.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class AddProductsRequestDTO {

    private List<ProductRequestPair> products;
}


