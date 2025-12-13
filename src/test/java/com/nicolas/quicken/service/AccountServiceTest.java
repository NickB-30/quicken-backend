package com.nicolas.quicken.service;

import com.nicolas.quicken.model.Transaction;
import com.nicolas.quicken.repository.AccountRepository;
import com.nicolas.quicken.repository.TransactionRepository;
import com.nicolas.quicken.service.AccountService.AccountSummary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/*
 * Unit tests for AccountService aggregation logic.
 * Tests verify correct calculation of income, expenses, and net values.
 */
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    /*
     * Test Case 1: Account summary with both income and expenses
     * Verifies correct separation and calculation of positive (income) and negative (expense) amounts
     */
    @Test
    public void testGetAccountSummaryIncomeAndExpenses() {
        // Setup test data
        Long accountId = 1L;
        LocalDate fromDate = LocalDate.of(2024, 1, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 31);

        // Create test transactions
        List<Transaction> transactions = Arrays.asList(
            new Transaction(1L, accountId, LocalDate.of(2024, 6, 1), new BigDecimal("1000.00"), "Salary"),
            new Transaction(2L, accountId, LocalDate.of(2024, 6, 5), new BigDecimal("-200.00"), "Rent"),
            new Transaction(3L, accountId, LocalDate.of(2024, 6, 10), new BigDecimal("-50.00"), "Groceries")
        );

        when(transactionRepository.findByAccountIdAndDateRange(accountId, fromDate, toDate))
            .thenReturn(transactions);

        // Execute
        AccountSummary summary = accountService.getAccountSummary(accountId, fromDate, toDate);

        // Verify calculations are correct
        assertEquals(new BigDecimal("1000.00"), summary.getTotalIncome());
        assertEquals(new BigDecimal("250.00"), summary.getTotalExpenses());
        assertEquals(new BigDecimal("750.00"), summary.getNet());
    }

    /*
     * Test Case 2: Account summary with no transactions (edge case)
     * Verifies system handles empty transaction lists without errors
     */
    @Test
    public void testGetAccountSummaryNoTransactions() {
        // Setup test data
        Long accountId = 1L;
        LocalDate fromDate = LocalDate.of(2024, 1, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 31);

        // Empty transactions list
        when(transactionRepository.findByAccountIdAndDateRange(accountId, fromDate, toDate))
            .thenReturn(Collections.emptyList());

        // Execute
        AccountSummary summary = accountService.getAccountSummary(accountId, fromDate, toDate);

        // Verify everything is zero
        assertEquals(BigDecimal.ZERO, summary.getTotalIncome());
        assertEquals(BigDecimal.ZERO, summary.getTotalExpenses());
        assertEquals(BigDecimal.ZERO, summary.getNet());
    }

    /*
     * Test Case 3: Account summary with only income transactions (edge case)
     * Verifies expenses remain zero when no negative amounts exist
     */
    @Test
    public void testGetAccountSummary_OnlyIncome() {
        // Setup test data
        Long accountId = 1L;
        LocalDate fromDate = LocalDate.of(2024, 1, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 31);

        // Create test transactions
        List<Transaction> transactions = Arrays.asList(
            new Transaction(1L, accountId, LocalDate.of(2024, 6, 1), new BigDecimal("500.00"), "Paycheck"),
            new Transaction(2L, accountId, LocalDate.of(2024, 6, 15), new BigDecimal("300.00"), "Bonus")
        );

        when(transactionRepository.findByAccountIdAndDateRange(accountId, fromDate, toDate))
            .thenReturn(transactions);

        // Execute
        AccountSummary summary = accountService.getAccountSummary(accountId, fromDate, toDate);

        // Verify calculations
        assertEquals(new BigDecimal("800.00"), summary.getTotalIncome());
        assertEquals(BigDecimal.ZERO, summary.getTotalExpenses());
        assertEquals(new BigDecimal("800.00"), summary.getNet());
    }
}