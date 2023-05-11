package com.banking.goal;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoalDTO {

    @NotBlank(message = "Goal Name should not be empty")
    private String goalName;
    @NotNull(message = "Goal Amount should not be empty")
    private Double goalAmount;
    @NotNull(message = "Account Number should not be empty")
    private Long accountNumber;

    private String serviceType;

    private String frequency;

    private String dayOfWeek;

    @FutureOrPresent(message = "Day of Month should be future or present")
    private LocalDate dayOfMonth;

}
