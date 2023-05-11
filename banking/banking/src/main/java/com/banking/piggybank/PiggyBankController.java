package com.banking.piggybank;

import com.banking.exception.BussinessException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/piggy-bank/")
public class PiggyBankController {

    @Autowired
    private PiggyBankService piggyBankService;

    @PostMapping("add")
    public ResponseEntity<PiggyBank> create(@Valid @RequestBody PiggyBankDTO piggyBankDTO) throws BussinessException {
        PiggyBank piggyBank = piggyBankService.createPiggyBank(piggyBankDTO);
        return new ResponseEntity<>(piggyBank, HttpStatus.OK);
    }

    @GetMapping("find")
    public ResponseEntity<PiggyBank> findByAccountNumber(@RequestParam("accountNumber") String accountNumber) throws BussinessException {
        PiggyBank piggyBank = piggyBankService.findByAccountNumber(Long.parseLong(accountNumber));
        return new ResponseEntity<>(piggyBank, HttpStatus.OK);
    }

}
