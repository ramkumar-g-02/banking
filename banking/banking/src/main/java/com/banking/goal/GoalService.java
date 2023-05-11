package com.banking.goal;

import com.banking.account.Account;
import com.banking.account.AccountService;
import com.banking.exception.BussinessException;
import com.banking.transaction.ServiceType;
import com.banking.transaction.Transaction;
import com.banking.transaction.TransactionDTO;
import com.banking.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class GoalService {
    @Autowired
    private GoalRepository goalRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;

    private Goal doGoalTransaction(GoalDTO goalDTO, Account account, Goal goal, String goalServiceType) throws BussinessException {
        if (goalDTO.getGoalAmount() <= 0) {
            throw new BussinessException("Amount should be greater than 0 ");
        }
        int dayOfWeekValue = -1;
        int dayOfMonthValue = -1;
        boolean hasFrequency = StringUtils.hasLength(goalDTO.getFrequency());
        if (hasFrequency) {
            if (GoalFrequency.WEEKLY.toString().equalsIgnoreCase(goalDTO.getFrequency())) {
                boolean hasDayOfWeek = StringUtils.hasLength(goalDTO.getDayOfWeek());
                Optional<DayOfWeek> dayOfWeek = Arrays.stream(DayOfWeek.values()).filter(day -> day.toString().equalsIgnoreCase(goalDTO.getDayOfWeek())).findFirst();
                if (hasDayOfWeek && dayOfWeek.isPresent()) {
                    dayOfWeekValue = dayOfWeek.get().getValue() % 7;
                } else {
                    throw new BussinessException("Invalid Day Of Week");
                }
            } else if (GoalFrequency.MONTHLY.toString().equalsIgnoreCase(goalDTO.getFrequency())) {
                dayOfMonthValue = goalDTO.getDayOfMonth().getDayOfMonth();
            }
        } else {
            throw new BussinessException("Frequency should not be empty");
        }

        if (goal == null) {
            goal = Goal.builder()
                    .goalName(goalDTO.getGoalName())
                    .goalAmount(0.0)
                    .account(account)
                    .frequency(goalDTO.getFrequency())
                    .dayOfWeek(dayOfWeekValue)
                    .dayOfMonth(dayOfMonthValue)
                    .build();
        }
        if (GoalServiceType.ADD_GOAL.toString().equals(goalServiceType)) {
            goal.doCredit(goalDTO.getGoalAmount());
        } else if (GoalServiceType.RELEASE_GOAL.toString().equals(goalServiceType)){
            goal.doDebit(goalDTO.getGoalAmount());
        }
        goalRepository.save(goal);

        TransactionDTO transactionDTO = TransactionDTO.builder()
                .fromAccount(account.getAccountNumber())
                .serviceType(ServiceType.GOAL_TRANSACTION.getServiceType())
                .amount(goalDTO.getGoalAmount())
                .password(account.getUser().getPassword())
                .goalServiceType(goalServiceType)
                .build();
        List<Transaction> transactions = transactionService.doTransaction(transactionDTO);
        Transaction transaction = transactions.get(0);
        if (!"Y".equals(transaction.getIsCompleted())) {
            if (GoalServiceType.ADD_GOAL.toString().equals(goalServiceType)) {
                goal.doDebit(goalDTO.getGoalAmount());
            } else if (GoalServiceType.RELEASE_GOAL.toString().equals(goalServiceType)){
                goal.doCredit(goalDTO.getGoalAmount());
            }
            goalRepository.save(goal);
            throw new BussinessException(transaction.getTransactionMessage());
        }
        return goal;
    }
    public Goal addGoal(GoalDTO goalDTO) throws BussinessException {
        Account account = accountService.getAccount(goalDTO.getAccountNumber());
        Goal goal = findGoal(goalDTO.getGoalName(), account.getAccountId());
        return doGoalTransaction(goalDTO, account, goal, GoalServiceType.ADD_GOAL.toString() );
    }

    public Goal releaseGoal(GoalDTO goalDTO) throws BussinessException {
        Account account = accountService.getAccount(goalDTO.getAccountNumber());
        Goal goal = findGoal(goalDTO.getGoalName(), account.getAccountId());
        if (goal == null) {
            throw new BussinessException("There is no goal with the name - '" + goalDTO.getGoalName() + "' exist for your account");
        }
        return doGoalTransaction(goalDTO, account, goal, GoalServiceType.RELEASE_GOAL.toString());
    }

    public List<Goal> findAllGoals(long accountNumber) throws BussinessException {
        Account account = accountService.getAccount(accountNumber);
        return goalRepository.findByAccount_AccountId(account.getAccountId());
    }

    public Goal findGoal(String goalName, long accountId) {
        return goalRepository.findByGoalNameIgnoreCaseAndAccount_AccountId(goalName, accountId);
    }

    public List<Goal> findAllGoals() throws BussinessException {
        return goalRepository.findAll();
    }

    public void addGoal(Goal goal) {
        try {
            doGoalTransaction(goal, GoalServiceType.ADD_GOAL.toString());
        } catch (BussinessException e) {
            e.printStackTrace();
        }
    }

    public void addGoal1(Goal goal) {
        try {
            doGoalTransaction(goal, GoalServiceType.ADD_GOAL.toString());
        } catch (BussinessException e) {
            e.printStackTrace();
        }
    }

    private Goal doGoalTransaction(Goal goal, String goalServiceType) throws BussinessException {
        Account account = goal.getAccount();
        if (GoalServiceType.ADD_GOAL.toString().equals(goalServiceType)) {
            goal.doCredit(goal.getGoalAmount());
        } else if (GoalServiceType.RELEASE_GOAL.toString().equals(goalServiceType)){
            goal.doDebit(goal.getGoalAmount());
        }
        goalRepository.save(goal);

        TransactionDTO transactionDTO = TransactionDTO.builder()
                .fromAccount(account.getAccountNumber())
                .serviceType(ServiceType.GOAL_TRANSACTION.getServiceType())
                .amount(goal.getGoalAmount())
                .password(account.getUser().getPassword())
                .goalServiceType(goalServiceType)
                .build();
        List<Transaction> transactions = transactionService.doTransaction(transactionDTO);
        Transaction transaction = transactions.get(0);
        if (!"Y".equals(transaction.getIsCompleted())) {
            if (GoalServiceType.ADD_GOAL.toString().equals(goalServiceType)) {
                goal.doDebit(goal.getGoalAmount());
            } else if (GoalServiceType.RELEASE_GOAL.toString().equals(goalServiceType)){
                goal.doCredit(goal.getGoalAmount());
            }
            goalRepository.save(goal);
            throw new BussinessException(transaction.getTransactionMessage());
        }
        return goal;
    }

}
