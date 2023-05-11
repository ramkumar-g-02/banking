package com.banking.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByFromAccount(long fromAccount);

    List<Transaction> findByFromAccountAndServiceTypeAndCreatedOnBetween(Long fromAccount, String serviceType, LocalDateTime createdOnStart, LocalDateTime createdOnEnd);



}
