package com.banking.piggybank;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PiggyBankRepository extends JpaRepository<PiggyBank, Long> {
    PiggyBank findByAccount_AccountNumber(Long accountNumber);

}
