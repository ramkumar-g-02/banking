package com.banking.account;

import com.banking.exception.BussinessException;
import com.banking.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "accounts", indexes = { @Index(name = "account_number_index", columnList = "accountNumber")}, uniqueConstraints = @UniqueConstraint(columnNames = "user_id"))
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    private String accountType;

    private Long accountNumber;

    private Boolean isActive;

    private String hasATMCard;

    private Double balance;

    private String branchName;

    @Embedded
    private IFSC ifsc;

    private LocalDateTime createdOn;

    private LocalDateTime lastModifiedOn;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;


    public Account doCredit(double amount) {
        synchronized (this) {
            this.balance += amount;
        }
        return this;
    }

    public Account doDebit(double amount) throws BussinessException {
        synchronized (this) {
            if (this.balance > 0 && amount <= this.balance) {
                this.balance -= amount;
            } else {
                throw new BussinessException("Your account is having only " + this.balance);
            }
        }
        return this;
    }

}
