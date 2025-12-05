package com.nicolas.quicken.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    // Transaction fields
    private Long id;
    private Long accountId; // Links to Account
    private LocalDate date;
    private BigDecimal amount;
    private String description;
}