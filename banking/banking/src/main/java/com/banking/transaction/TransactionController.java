package com.banking.transaction;

import com.banking.exception.BussinessException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction/")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("create")
    public ResponseEntity<List<Transaction>> doTransaction(@Valid @RequestBody TransactionDTO transactionDTO) throws BussinessException {
        List<Transaction> transactions = transactionService.doTransaction(transactionDTO);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("all-transaction")
    public ResponseEntity<List<Transaction>> findAllTransaction(@RequestParam("accountNumber") String accountNumber) {
        List<Transaction> transactions = transactionService.getAllTransaction(Long.parseLong(accountNumber));
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

}
