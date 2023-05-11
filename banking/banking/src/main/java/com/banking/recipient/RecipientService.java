package com.banking.recipient;

import com.banking.account.Account;
import com.banking.account.AccountService;
import com.banking.exception.BussinessException;
import com.banking.user.User;
import com.banking.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class RecipientService {
    @Autowired
    private RecipientRepository recipientRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;


    public Recipient addRecipient(RecipientDTO recipientDTO) throws BussinessException {
        User user = userService.findUser(recipientDTO.getUserId());
        Account account = accountService.getAccount(recipientDTO.getRecipientAccountNumber());
        if (account.getUser().getUserId().equals(user.getUserId())) {
            throw new BussinessException("You can not be a recipient for yourself");
        }
        boolean isActive = recipientDTO.getIsActive() != null ? recipientDTO.getIsActive() : true;
        Recipient recipient = Recipient.builder()
                .user(user)
                .recipientName(recipientDTO.getRecipientName())
                .isActive(isActive)
                .account(account)
                .build();
        return recipientRepository.save(recipient);
    }

    public Recipient removeRecipient(long recipientAccountNumber, long userId) throws BussinessException {
        User user = userService.findUser(userId);
        Account account = accountService.getAccount(recipientAccountNumber);
        Optional<Recipient> optionalRecipient = recipientRepository.findByUser_UserIdAndAccount_AccountNumber(userId, account.getAccountNumber());
        if (!optionalRecipient.isPresent()) {
            throw new BussinessException("Recipient does not exist for the user id " + userId);
        }
        Recipient recipient = optionalRecipient.get();
        recipientRepository.deleteById(recipient.getRecipientId());
        return recipient;
    }

    public List<Recipient> findAllRecipient(long userId) throws BussinessException {
        userService.findUser(userId);
        return recipientRepository.findByUser_UserId(userId);
    }

    public Recipient updateRecipient(RecipientDTO recipientDTO) throws BussinessException {
        Optional<Recipient> optionalRecipient = recipientRepository.findByUser_UserIdAndAccount_AccountNumber(recipientDTO.getUserId(), recipientDTO.getRecipientAccountNumber());
        Recipient recipient = null;
        if (!optionalRecipient.isPresent()) {
            throw new BussinessException("Recipient does not exist");
        } else {
            recipient = optionalRecipient.get();
            if (recipientDTO.getIsActive() != null) {
                recipient.setIsActive(recipientDTO.getIsActive());
            }
            if (StringUtils.hasLength(recipientDTO.getRecipientName())) {
                recipient.setRecipientName(recipientDTO.getRecipientName());
            }
            recipientRepository.save(recipient);
        }
        return recipient;
    }

    public Recipient findRecipient(long recipientId, long userId) throws BussinessException {
        Optional<Recipient> optionalRecipientById = recipientRepository.findById(recipientId);
        if (!optionalRecipientById.isPresent()) {
            throw new BussinessException("Three is no Recipient with id " + recipientId);
        }
        Optional<Recipient> optionalRecipient = recipientRepository.findByRecipientIdAndUser_UserId(recipientId, userId);
        Recipient recipient = null;
        if (!optionalRecipient.isPresent()) {
            throw new BussinessException("You don't have the recipient with id " + recipientId);
        }
        return optionalRecipient.get();
    }

}
