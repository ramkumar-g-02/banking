package com.banking.piggytranaction;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PiggyTransactionRepository extends JpaRepository<PiggyTransaction, Long> {
}
