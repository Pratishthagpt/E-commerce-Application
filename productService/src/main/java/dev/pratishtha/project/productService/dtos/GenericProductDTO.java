package dev.pratishtha.project.productService.dtos;

import dev.pratishtha.project.productService.models.Category;
import dev.pratishtha.project.productService.models.Price;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericProductDTO {

    private String id;
    private String title;
    private String description;
    private String image;
    private String category_name;
    private double priceVal;
}
