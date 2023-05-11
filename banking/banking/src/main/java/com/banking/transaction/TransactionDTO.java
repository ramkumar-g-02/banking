package com.banking.transaction;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDTO {

    @NotNull(message = "From Account should not be empty")
    private Long fromAccount;

    private Long toAccount;

    @NotNull(message = "Amount should not be empty")
    private Double amount;

    @NotEmpty(message = "Service Type should not be empty")
    private String serviceType;

    private String comments;

    @NotEmpty(message = "Password should not be empty")
    private String password;

    private Long recipientId;

    private String goalServiceType;

}
