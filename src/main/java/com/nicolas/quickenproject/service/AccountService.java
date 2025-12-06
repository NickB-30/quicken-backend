package com.nicolas.quicken.service;

import com.nicolas.quicken.model.Account;
import com.nicolas.quicken.model.Transaction;
import com.nicolas.quicken.repository.AccountRepository;
import com.nicolas.quicken.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // Get all accounts
    public List<Account> getAllAccounts() {
        return accountRepository.findAllAccounts();
    }

    // Get account summary for a date range
    public AccountSummary getAccountSummary(Long accountId, LocalDate fromDate, LocalDate toDate) {
        List<Transaction> transactions = transactionRepository.findByAccountIdAndDateRange(accountId, fromDate, toDate);

        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpenses = BigDecimal.ZERO;

        // Loop through transactions to determine income and expense amounts
        for (Transaction transaction : transactions) {
            BigDecimal amount = transaction.getAmount();
            if (amount.compareTo(BigDecimal.ZERO) > 0) {
                // Positive amount = income
                totalIncome = totalIncome.add(amount);
            } else {
                // Negative amount = expense
                totalExpenses = totalExpenses.add(amount.abs());
            }
        }

        // Caluclate net 
        BigDecimal net = totalIncome.subtract(totalExpenses);
        
        // Return AccountSummary
        return new AccountSummary(totalIncome, totalExpenses, net);
    }

    // Class to represent summary data
    @Data
    @AllArgsConstructor
    public static class AccountSummary {
        private BigDecimal totalIncome;
        private BigDecimal totalExpenses;
        private BigDecimal net;
    }
}