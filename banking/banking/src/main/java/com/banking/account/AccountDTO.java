package com.banking.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {

    private String accountType;

    @NotBlank(message = "Branch Name should not be empty")
    private String branchName;

    @NotNull(message = "IFSC should not be null")
    private IFSC ifsc;

    @NotNull(message = "User Id should not be empty")
    private Long userId;

    private String needATMCard;

    private double balance;

}
