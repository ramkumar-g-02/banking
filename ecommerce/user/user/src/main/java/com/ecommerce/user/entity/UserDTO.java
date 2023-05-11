package com.ecommerce.user.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
public class UserDTO {

    @NotBlank(message = "Name should not be empty")
    private String name;

    @Email(message = "Invalid Email")
    @NotBlank(message = "Email should not empty")
    private String email;

    @NotBlank(message = "Password should not be empty")
    private String password;
    @NotNull(message = "Address should not be empty")
    private Address address;

}
