package com.banking.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

//    @Query(value = "select nextval('account_sequence') ", nativeQuery = true)
//    Long getNextAccountSequence();

    Account findByAccountNumber(Long accountNumber);

    List<Account> findByBranchNameLikeAndUser_DateOfBirthAfter(String branchName, LocalDate dateOfBirth);

    Account findByUser_UserId(Long userId);




}
