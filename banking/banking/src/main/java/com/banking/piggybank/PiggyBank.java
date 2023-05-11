package com.banking.piggybank;

import com.banking.account.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "piggy_banks", uniqueConstraints = @UniqueConstraint(columnNames = "account_id"))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PiggyBank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double balance;

    @OneToOne
    @JoinColumn(name = "account_id", referencedColumnName = "accountId")
    private Account account;

    private LocalDateTime createdOn;

    private LocalDateTime lastModifiedOn;

    public PiggyBank doCredit(double amount) {
        synchronized (this) {
            this.balance += amount;
        }
        return this;
    }

}
