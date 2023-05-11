package com.banking.goal;

import com.banking.account.Account;
import com.banking.exception.BussinessException;
import com.banking.piggybank.PiggyBank;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "goals")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goalId;

    private String goalName;

    private Double goalAmount;
    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "accountId")
    private Account account;

    private String frequency;

    private int dayOfWeek;

    private int dayOfMonth;

    private LocalDate scheduledDate;

    public Goal doCredit(double amount) {
        synchronized (this) {
            this.goalAmount += amount;
        }
        return this;
    }

    public Goal doDebit(double amount) throws BussinessException {
        synchronized (this) {
            if (this.goalAmount > 0 && amount <= this.goalAmount) {
                this.goalAmount -= amount;
            } else {
                throw new BussinessException("Your Goal name - '" + this.goalName + "' is having only " + this.goalAmount);
            }
        }
        return this;
    }

}
