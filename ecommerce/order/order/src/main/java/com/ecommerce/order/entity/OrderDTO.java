package com.ecommerce.order.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderDTO {

    @NotNull(message = "Cart Id should not be empty")
    private Long cartId;
    @NotNull(message = "Quantity should not be empty")
    private Integer quantity;
    @NotBlank(message = "Payment Method should not be empty")
    private String paymentMethod;

}
