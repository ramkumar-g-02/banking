package com.banking.goal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {

    Goal findByGoalNameIgnoreCaseAndAccount_AccountId(String goalName, Long accountId);

    List<Goal> findByAccount_AccountId(Long accountId);

}
