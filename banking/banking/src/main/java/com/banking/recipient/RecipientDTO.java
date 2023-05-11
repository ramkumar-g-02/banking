package com.banking.recipient;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipientDTO {

    @NotNull(message = "User Id should not be empty")
    private Long userId;

    @NotNull(message = "Recipient Account Number should not be empty")
    private Long recipientAccountNumber;

    @NotBlank(message = "Recipient Name should not be empty")
    private String recipientName;

    private Boolean isActive;

}
