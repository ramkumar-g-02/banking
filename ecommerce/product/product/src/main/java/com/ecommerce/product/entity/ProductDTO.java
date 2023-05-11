package com.ecommerce.product.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;


@Data
public class ProductDTO {

    @NotBlank(message = "Product Name should not be empty")
    private String name;

    @NotBlank(message = "Product Description should not be empty")
    private String description;

    @NotNull(message = "Price should not be empty")
    @Positive(message = "Price should be greater than 0 ")
    private Double price;

    @NotBlank(message = "Category should not be empty")
    private String category;

    @Positive(message = "Quantity should be greater than 0 ")
    @NotNull(message = "Price should not be empty")
    private Integer quantity;

    private Integer rating;

}
