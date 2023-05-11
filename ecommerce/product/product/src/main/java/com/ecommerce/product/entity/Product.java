package com.ecommerce.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String name;

    private String description;

    private double price;

    private String category;

    private int rating;

    @Transient
    private boolean inStock;

    private int quantity;

    private LocalDateTime createdOn;

    private LocalDateTime modifiedOn;

    public boolean getInStock() {
        return quantity > 0;
    }

}
