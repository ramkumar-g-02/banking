package com.banking.piggybank;

import com.banking.account.Account;
import com.banking.account.AccountRepository;
import com.banking.exception.BussinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service

//@PropertySources({
//        @PropertySource("classpath:message_fr.properties"),
//        @PropertySource("classpath:message.properties"),
//
//})
public class PiggyBankService {
    @Autowired
    private PiggyBankRepository piggyBankRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Value("${piggy_bank.exist}")
    private String message = null;

    public PiggyBank createPiggyBank(PiggyBankDTO piggyBankDTO) throws BussinessException {
        Account account = piggyBankDTO.getAccount();
        if (account.getAccountNumber() == null) {
            throw new BussinessException("Account Number should not be empty");
        }
        account = accountRepository.findByAccountNumber(piggyBankDTO.getAccount().getAccountNumber());
        if (account == null) {
            throw new BussinessException("Account does not exist");
        }
        PiggyBank existingPiggyBank = piggyBankRepository.findByAccount_AccountNumber(piggyBankDTO.getAccount().getAccountNumber());

        if (existingPiggyBank != null) {
            throw new BussinessException(message);
        }
        PiggyBank piggyBank = PiggyBank.builder()
                .createdOn(LocalDateTime.now())
                .lastModifiedOn(LocalDateTime.now())
                .balance(Math.max(piggyBankDTO.getAmount(), 0.0))
                .account(account)
                .build();
        return piggyBankRepository.save(piggyBank);
    }

    public PiggyBank findByAccountNumber(long accountNumber) throws BussinessException {
        PiggyBank piggyBank = piggyBankRepository.findByAccount_AccountNumber(accountNumber);
        if (piggyBank != null) {
            return piggyBank;
        } else {
            throw new BussinessException("Piggy Bank does not exist");
        }
    }
}
