package com.banking.account;

import com.banking.exception.BussinessException;
import com.banking.user.User;
import com.banking.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(AccountDTO accountDTO) throws BussinessException {
        IFSC ifsc = accountDTO.getIfsc();
        if (!StringUtils.hasLength(ifsc.getIfscCode())) {
            throw new BussinessException("IFSC Code is mandatory");
        }
        Optional<User> user = userRepository.findById(accountDTO.getUserId());
        if (!user.isPresent()) {
            throw new BussinessException("User does not exist");
        }
        Account existingAccount = accountRepository.findByUser_UserId(accountDTO.getUserId());
        if (existingAccount != null) {
            throw new BussinessException("Account already exist for the user " + accountDTO.getUserId());
        }
        Account account = new Account();
        if (accountDTO.getAccountType() != null && AccountType.CURRENT.toString().equalsIgnoreCase(accountDTO.getAccountType())) {
            account.setAccountType(AccountType.CURRENT.toString());
        } else {
            account.setAccountType(AccountType.SAVINGS.toString());
        }
        account.setUser(user.get());
        account.setIfsc(accountDTO.getIfsc());
        account.setBranchName(accountDTO.getBranchName());
        account.setIsActive(true);

        account.setCreatedOn(LocalDateTime.now());
        account.setLastModifiedOn(LocalDateTime.now());
        if (StringUtils.hasLength(accountDTO.getNeedATMCard()) && "Y".equals(accountDTO.getNeedATMCard())) {
            account.setHasATMCard("Y");
        } else {
            account.setHasATMCard("N");
        }
        account.setBalance(Math.max(accountDTO.getBalance(), 0));
        accountRepository.save(account);

        /* Adding Account Number based on IFSC Code */
        account.setAccountNumber(getAccountNumber(account.getIfsc(), account.getAccountId()));
        accountRepository.save(account);

        AccountUtil.getAccounts().add(account);
        return account;
    }

    private Long getAccountNumber(IFSC ifsc, Long accountId) {
        String accountNumber = ifsc.getBranchCode() + accountId;
        return Long.parseLong(accountNumber);
    }

    public Account getAccount(long accountNumber) throws BussinessException {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account != null) {
            return account;
        } else {
            throw new BussinessException("Account number " + accountNumber + " does not exist");
        }
    }

    public List<Account> findByBranchAndDateOfBirth(String branchName, LocalDate dateOfBirth) {
        return accountRepository.findByBranchNameLikeAndUser_DateOfBirthAfter(branchName, dateOfBirth);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account inActiveAccount(long accountNUmber) throws BussinessException {
        Account account = getAccount(accountNUmber);
        account.setIsActive(false);
        accountRepository.save(account);
        User user = account.getUser();
        user.setLastModifiedOn(LocalDateTime.now());
        userRepository.save(user);
        return account;
    }

}
