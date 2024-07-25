package dev.pratishtha.project.CartService.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

public class Product {

    private String productId;
    private String title;
    private String description;
    private String image;

    private String category;

    private int price;

    private int inventoryCount;
}
