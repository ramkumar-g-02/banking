package com.ecommerce.cart.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartDTO {

    @NotNull(message = "Product Id should not empty")
    private Long productId;
    @NotNull(message = "User Id should not empty")
    private Long userId;

}
