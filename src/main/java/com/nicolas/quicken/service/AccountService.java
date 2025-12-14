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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    // Get daily summary for a date range
    public List<DailySummary> getDailySummary(Long accountId, LocalDate fromDate, LocalDate toDate) {
        List<Transaction> transactions = transactionRepository.findByAccountIdAndDateRange(accountId, fromDate, toDate);
        
        // Group transactions by date
        Map<LocalDate, List<Transaction>> transactionsByDate = new HashMap<>();
        for (Transaction transaction : transactions) {
            LocalDate date = transaction.getDate();
            transactionsByDate.computeIfAbsent(date, k -> new ArrayList<>()).add(transaction);
        }
        
        // Calculate daily summaries
        List<DailySummary> dailySummaries = new ArrayList<>();
        for (Map.Entry<LocalDate, List<Transaction>> entry : transactionsByDate.entrySet()) {
            LocalDate date = entry.getKey();
            List<Transaction> dayTransactions = entry.getValue();
            
            BigDecimal dailyIncome = BigDecimal.ZERO;
            BigDecimal dailyExpenses = BigDecimal.ZERO;
            
            // Loop through each transaction to check for positive or negative amount
            for (Transaction transaction : dayTransactions) {
                BigDecimal amount = transaction.getAmount();
                if (amount.compareTo(BigDecimal.ZERO) > 0) {
                    dailyIncome = dailyIncome.add(amount);
                } else {
                    dailyExpenses = dailyExpenses.add(amount.abs());
                }
            }
            
            BigDecimal dailyNet = dailyIncome.subtract(dailyExpenses);
            dailySummaries.add(new DailySummary(date, dailyIncome, dailyExpenses, dailyNet));
        }
        
        // Sort by date
        dailySummaries.sort((a, b) -> a.getDate().compareTo(b.getDate()));
        
        return dailySummaries;
    }

    // Class to represent account summary data
    @Data
    @AllArgsConstructor
    public static class AccountSummary {
        private BigDecimal totalIncome;
        private BigDecimal totalExpenses;
        private BigDecimal net;
    }

    // Class to represent daily summary data
    @Data
    @AllArgsConstructor
    public static class DailySummary {
        private LocalDate date;
        private BigDecimal income;
        private BigDecimal expenses;
        private BigDecimal net;
    }
}