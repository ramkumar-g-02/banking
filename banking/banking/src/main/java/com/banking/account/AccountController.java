package com.banking.account;

import com.banking.exception.BussinessException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/account/")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("create")
    public ResponseEntity<Account> createAccount(@Valid @RequestBody AccountDTO accountDTO) throws BussinessException {
        Account account = accountService.createAccount(accountDTO);
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    @GetMapping("get/{accountNo}")
    public ResponseEntity<Account> getAccount(@PathVariable String accountNo) throws BussinessException {
        Account account = accountService.getAccount(Long.parseLong(accountNo));
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @GetMapping("find-branch-dob")
    public ResponseEntity<List<Account>> findByBranchAndDateOfBirth(@RequestParam("branch") String branchName, @RequestParam("dob") String dateOfBirth) {
        List<Account> accounts = accountService.findByBranchAndDateOfBirth(branchName, LocalDate.parse(dateOfBirth));
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @GetMapping("all-account")
    public ResponseEntity<List<Account>> findByAllAccount() {
        List<Account> accounts = accountService.getAllAccounts();
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @GetMapping("block")
    public ResponseEntity<Account> inActiveAccount(@RequestParam("accountNumber") String accountNumber) throws BussinessException {
        Account account = accountService.inActiveAccount(Long.parseLong(accountNumber));
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

}
