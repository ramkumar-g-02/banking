package com.banking.recipient;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecipientRepository extends JpaRepository<Recipient, Long> {
    Optional<Recipient> findByUser_UserIdAndAccount_AccountNumber(Long userId, Long accountNumber);

    List<Recipient> findByUser_UserId(Long userId);

    Optional<Recipient> findByRecipientIdAndUser_UserId(Long recipientId, Long userId);


}
