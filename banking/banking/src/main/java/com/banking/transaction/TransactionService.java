package com.banking.transaction;

import com.banking.account.Account;
import com.banking.account.AccountRepository;
import com.banking.account.AccountUtil;
import com.banking.constants.BankConstants;
import com.banking.exception.BussinessException;
import com.banking.goal.GoalServiceType;
import com.banking.piggybank.PiggyBank;
import com.banking.piggybank.PiggyBankRepository;
import com.banking.piggytranaction.PiggyTransaction;
import com.banking.piggytranaction.PiggyTransactionRepository;
import com.banking.recipient.Recipient;
import com.banking.recipient.RecipientService;
import com.banking.user.User;
import com.banking.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PiggyBankRepository piggyBankRepository;

    @Autowired
    private PiggyTransactionRepository piggyTransactionRepository;
    @Autowired
    private RecipientService recipientService;

    public List<Transaction> doTransaction(TransactionDTO transactionDTO) throws BussinessException {
        String serviceType = transactionDTO.getServiceType();
        List<Transaction> transactions = null;
        if (ServiceType.ATM_TRANSACTION.getServiceType().equalsIgnoreCase(serviceType)) {
            transactions = doATMTransaction(transactionDTO);
        } else if (ServiceType.UPI_TRANSACTION.getServiceType().equalsIgnoreCase(serviceType)) {
            transactions = doUPITransaction(transactionDTO);
        } else if (ServiceType.GOAL_TRANSACTION.getServiceType().equalsIgnoreCase(serviceType)) {
            transactions = doGoalTransaction(transactionDTO);
        }
        return transactions;
    }

    private List<Transaction> doGoalTransaction(TransactionDTO transactionDTO) throws BussinessException {
        Account account = getAccount(transactionDTO.getFromAccount(), "Your account");
        User user = account.getUser();
        Transaction transaction = Transaction.builder()
                .account(account)
                .fromAccount(account.getAccountNumber())
                .amount(transactionDTO.getAmount())
                .createdOn(LocalDateTime.now())
                .serviceType(transactionDTO.getServiceType())
                .transactionMessage("Completed")
                .build();
        try {
//            doTransactionValidations(transactionDTO, account);
            if (GoalServiceType.ADD_GOAL.toString().equals(transactionDTO.getGoalServiceType())) {
                account = account.doDebit(transactionDTO.getAmount());
                transaction.setCreditOrDebit("DEBIT");
            } else if (GoalServiceType.RELEASE_GOAL.toString().equals(transactionDTO.getGoalServiceType())) {
                account = account.doCredit(transactionDTO.getAmount());
                transaction.setCreditOrDebit("CREDIT");
            }
            transaction.setIsCompleted("Y");
            account.setLastModifiedOn(LocalDateTime.now());
            user.setLastModifiedOn(LocalDateTime.now());
            account.setUser(user);
        } catch (BussinessException bussinessException) {
            transaction.setIsCompleted("N");
            transaction.setTransactionMessage(bussinessException.getErrorMessage());
        }
        userRepository.save(user);

        accountRepository.save(account);

        transactionRepository.save(transaction);
        return List.of(transaction);
    }

    private List<Transaction> doUPITransaction(TransactionDTO transactionDTO) throws BussinessException {
        Account fromAccount = getAccount(transactionDTO.getFromAccount(), "From account");
        Account toAccount = null;
        double piggyBankAmount = 0.0;

        // Checking for whether i need to use toAccount or recipientId
        if (transactionDTO.getToAccount() != null && transactionDTO.getRecipientId() != null) {
            throw new BussinessException("You can not have both To Account and Recipient Account ");
        }
        if (transactionDTO.getToAccount() != null) {
            toAccount = getAccount(transactionDTO.getToAccount(), "To account");
        } else {
            Recipient recipient = recipientService.findRecipient(transactionDTO.getRecipientId(), fromAccount.getUser().getUserId());
            if (!recipient.getIsActive()) {
                throw new BussinessException("Recipient is InActive");
            }
            toAccount = recipient.getAccount();
        }

        PiggyBank piggyBank = piggyBankRepository.findByAccount_AccountNumber(fromAccount.getAccountNumber());
        if (piggyBank == null) {
            throw new BussinessException("Piggy does not exist for account - " + fromAccount.getAccountNumber());
        }
        if (fromAccount.getAccountNumber().equals(toAccount.getAccountNumber())) {
            throw new BussinessException("Self transaction not allowed ");
        }
        if (transactionDTO.getAmount() <= 0) {
            throw new BussinessException("Amount should be greater than 0 ");
        }
        User fromUser = fromAccount.getUser();
        User toUser = toAccount.getUser();
        if (!StringUtils.hasLength(fromUser.getPassword()) || !fromUser.getPassword().equals(transactionDTO.getPassword()))
            throw new BussinessException("Invalid Password");

        Transaction fromTransaction = Transaction.builder()
                .account(fromAccount)
                .fromAccount(fromAccount.getAccountNumber())
                .toAccount(toAccount.getAccountNumber())
                .amount(transactionDTO.getAmount())
                .createdOn(LocalDateTime.now())
                .comments(transactionDTO.getComments())
                .serviceType(transactionDTO.getServiceType())
                .creditOrDebit("DEBIT")
                .transactionMessage("Completed")
                .build();

        /* Transaction for From Account */
        try {
            doTransactionValidations(transactionDTO, fromAccount);
            piggyBankAmount = getPiggyBankAmount(transactionDTO, piggyBankAmount);
            fromAccount = fromAccount.doDebit(transactionDTO.getAmount() + piggyBankAmount);
            fromTransaction.setIsCompleted("Y");
            fromAccount.setLastModifiedOn(LocalDateTime.now());
            fromUser.setLastModifiedOn(LocalDateTime.now());
            fromAccount.setUser(fromUser);
        } catch (BussinessException bussinessException) {
            fromTransaction.setIsCompleted("N");
            fromTransaction.setTransactionMessage(bussinessException.getErrorMessage());
        }

        Transaction toTransaction = Transaction.builder()
                .account(toAccount)
                .fromAccount(fromAccount.getAccountNumber())
                .toAccount(toAccount.getAccountNumber())
                .amount(transactionDTO.getAmount())
                .createdOn(LocalDateTime.now())
                .comments(transactionDTO.getComments())
                .serviceType(transactionDTO.getServiceType())
                .creditOrDebit("CREDIT")
                .transactionMessage("Completed")
                .build();

        /* Transaction for To Account */
        try {
            boolean isActive = toAccount.getIsActive();
            if (!isActive) {
                throw new BussinessException("To account is InActive");
            }
            if (!StringUtils.hasLength(fromTransaction.getIsCompleted()) || "N".equals(fromTransaction.getIsCompleted())) {
                throw new BussinessException(fromTransaction.getTransactionMessage());
            }
            toAccount = toAccount.doCredit(transactionDTO.getAmount());
            toTransaction.setIsCompleted("Y");
            toAccount.setLastModifiedOn(LocalDateTime.now());
            toUser.setLastModifiedOn(LocalDateTime.now());
            toAccount.setUser(toUser);

        } catch (BussinessException bussinessException) {
            toTransaction.setIsCompleted("N");
            toTransaction.setTransactionMessage(bussinessException.getErrorMessage());
            fromAccount = fromAccount.doCredit(transactionDTO.getAmount() + piggyBankAmount);
        }

        userRepository.save(fromUser);
        userRepository.save(toUser);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        transactionRepository.save(fromTransaction);
        transactionRepository.save(toTransaction);

        if (piggyBankAmount > 0.0) {
            piggyBank.setLastModifiedOn(LocalDateTime.now());
            piggyBank.doCredit(piggyBankAmount);
            piggyBankRepository.save(piggyBank);
            PiggyTransaction piggyTransaction = PiggyTransaction.builder()
                    .amount(piggyBankAmount)
                    .createdOn(LocalDateTime.now())
                    .transaction(fromTransaction)
                    .build();
            piggyTransactionRepository.save(piggyTransaction);
        }

        return List.of(fromTransaction, toTransaction);
    }

    private double getPiggyBankAmount(TransactionDTO transactionDTO, double piggyBankAmount) {
        Double amount = transactionDTO.getAmount();
        BigDecimal decimal = new BigDecimal(Double.toString(amount - Math.floor(amount)));
        decimal = decimal.setScale(2, RoundingMode.HALF_DOWN);
        if (decimal.doubleValue() < 0.5 && decimal.doubleValue() > 0) {
            double floor = Math.floor(amount) + 0.5;
            decimal = new BigDecimal(Double.toString(floor-amount));
            decimal = decimal.setScale(2, RoundingMode.HALF_DOWN);
            piggyBankAmount = decimal.doubleValue();
        } else if (decimal.doubleValue() >= 0.5) {
            double ceil = Math.ceil(amount) - amount;
            BigDecimal bd1 = new BigDecimal(Double.toString(ceil));
            bd1 = bd1.setScale(2, RoundingMode.HALF_DOWN);
            piggyBankAmount = bd1.doubleValue();
        }
        return piggyBankAmount;
    }

    private List<Transaction> doATMTransaction(TransactionDTO transactionDTO) throws BussinessException {
        Account fromAccount = getAccount(transactionDTO.getFromAccount(), "From account");
        User user = fromAccount.getUser();
        if (!StringUtils.hasLength(user.getPassword()) || !user.getPassword().equals(transactionDTO.getPassword()))
            throw new BussinessException("Invalid Password");
        doATMTransactionValidations(transactionDTO, fromAccount);
        validateMonthlyDebitLimits(transactionDTO);
        fromAccount = fromAccount.doDebit(transactionDTO.getAmount());
        fromAccount.setLastModifiedOn(LocalDateTime.now());
        user.setLastModifiedOn(LocalDateTime.now());
        Transaction transaction = Transaction.builder()
                .account(fromAccount)
                .fromAccount(fromAccount.getAccountNumber())
                .isCompleted("Y")
                .transactionMessage("Completed")
                .createdOn(LocalDateTime.now())
                .amount(transactionDTO.getAmount())
                .serviceType(transactionDTO.getServiceType())
                .creditOrDebit("DEBIT")
                .build();
        userRepository.save(user);
        accountRepository.save(fromAccount);
        transactionRepository.save(transaction);
        return List.of(transaction);
    }

    private void validateMonthlyDebitLimits(TransactionDTO transactionDTO) throws BussinessException {
        LocalDateTime startTime = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), 01, 0, 0, 0);
        List<Transaction> transactions = transactionRepository.findByFromAccountAndServiceTypeAndCreatedOnBetween(transactionDTO.getFromAccount(), transactionDTO.getServiceType(), startTime, LocalDateTime.now());
        double currentMonthTransactionAmount = transactions.stream().mapToDouble(Transaction::getAmount).sum();
        if (currentMonthTransactionAmount == BankConstants.DEBIT_LIMIT)
            throw new BussinessException("Your debit limit is exceeded" + " for this month");
        if ((currentMonthTransactionAmount + transactionDTO.getAmount()) > BankConstants.DEBIT_LIMIT)
            throw new BussinessException("You are exceeding debit limit " + BankConstants.DEBIT_LIMIT + " for this month");
    }

    private void doATMTransactionValidations(TransactionDTO transactionDTO, Account fromAccount) throws BussinessException {
        doTransactionValidations(transactionDTO, fromAccount);
        doATMCardValidations(fromAccount);
    }

    private void doATMCardValidations(Account fromAccount) throws BussinessException {
        String hasATMCard = fromAccount.getHasATMCard();
        if (!StringUtils.hasLength(hasATMCard) || !"Y".equalsIgnoreCase(hasATMCard))
            throw new BussinessException("You don't have a ATM Card");
    }

    private void doTransactionValidations(TransactionDTO transactionDTO, Account fromAccount) throws BussinessException {
        boolean isActive = fromAccount.getIsActive();
        if (!isActive)
            throw new BussinessException("Your account is InActive");
        double balance = fromAccount.getBalance();
        boolean hasEnoughBalance = balance >= transactionDTO.getAmount();
        if (!hasEnoughBalance)
            throw new BussinessException("You don't have enough balance");
    }

    private Account getAccount(Long accountNumber, String accountUser) throws BussinessException {
        Optional<Account> optionalAccount = AccountUtil.getAccounts().stream().filter(account -> account.getAccountNumber().equals(accountNumber)).findAny();
        if (optionalAccount.isPresent()) {
            return optionalAccount.get();
        } else {
            throw new BussinessException(accountUser + " does not exist");
        }
    }

    public List<Transaction> getAllTransaction(long accountNumber) {
        List<Transaction> transactions = transactionRepository.findByFromAccount(accountNumber);
        return transactions;
    }

}
