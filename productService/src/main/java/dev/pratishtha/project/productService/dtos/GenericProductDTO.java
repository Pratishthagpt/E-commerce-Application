package dev.pratishtha.project.productService.dtos;

import dev.pratishtha.project.productService.models.Category;
import dev.pratishtha.project.productService.models.Price;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GenericProductDTO implements Serializable {

    private String id;
    private String title;
    private String description;
    private String image;
    private String category_name;
    private double priceVal;
    private int inventoryCount;
}
