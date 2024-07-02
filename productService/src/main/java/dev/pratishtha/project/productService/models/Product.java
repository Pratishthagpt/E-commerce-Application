package dev.pratishtha.project.productService.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.engine.internal.Cascade;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseModel{

    private String title;
    private String description;
    private String image;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category")
    private Category category;

    @OneToOne(cascade = CascadeType.REMOVE)
    private Price price;

    private int inventoryCount;

}
