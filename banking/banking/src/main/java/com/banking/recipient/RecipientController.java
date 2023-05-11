package com.banking.recipient;

import com.banking.exception.BussinessException;
import com.banking.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipient")
public class RecipientController {

    @Autowired
    private RecipientService recipientService;
    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<Recipient> addRecipient(@Valid @RequestBody RecipientDTO recipientDTO) throws BussinessException {
        Recipient recipient = recipientService.addRecipient(recipientDTO);
        return new ResponseEntity<>(recipient, HttpStatus.OK);
    }

    @GetMapping("/remove")
    public ResponseEntity<String> removeRecipient(@RequestParam("recipientAccountNumber") String recipientAccountNumber, @RequestParam("userId") String userId) throws BussinessException {
        Recipient recipient = recipientService.removeRecipient(Long.parseLong(recipientAccountNumber), Long.parseLong(userId));
        return new ResponseEntity<>("Your Recipient " + recipient.getRecipientName() + " is deleted successfully ", HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Recipient>> findAllRecipient(@RequestParam("userId") String userId) throws BussinessException {
        List<Recipient> recipients = recipientService.findAllRecipient(Long.parseLong(userId));
        return new ResponseEntity<>(recipients, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Recipient> updateRecipient(@Valid @RequestBody RecipientDTO recipientDTO) throws BussinessException {
        Recipient recipient = recipientService.updateRecipient(recipientDTO);
        return new ResponseEntity<>(recipient, HttpStatus.OK);
    }

}
