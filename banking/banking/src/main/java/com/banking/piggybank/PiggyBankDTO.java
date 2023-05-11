package com.banking.piggybank;

import com.banking.account.Account;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PiggyBankDTO {

    @NotNull(message = "Account should not be empty")
    private Account account;

    private double amount;

}
