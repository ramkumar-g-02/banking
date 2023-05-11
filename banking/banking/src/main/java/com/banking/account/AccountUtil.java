package com.banking.account;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountUtil {
    private static List<Account> accounts = null;
    @Autowired
    private AccountRepository accountRepository;

    @PostConstruct
    private void allAccounts() {
        accounts = accountRepository.findAll();
    }

    public static List<Account> getAccounts() {
        return accounts;
    }
}
