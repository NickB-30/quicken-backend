package com.nicolas.quicken.controller;

import com.nicolas.quicken.model.Account;
import com.nicolas.quicken.service.AccountService;
import com.nicolas.quicken.service.AccountService.AccountSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    
    @Autowired
    private AccountService accountService;

    // GET /api/accounts - List all accounts
    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    // GET /api/accounts/{accountId}/summary?from=YYYY-MM-DD&to=YYYY-MM-DD
    @GetMapping("/{accountId}/summary")
    public AccountSummary getAccountSummary(
        @PathVariable Long accountId,
        // Query Parameters
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return accountService.getAccountSummary(accountId, from, to);
    }

    // GET /api/accounts/{accountId}/daily-summary?from=YYYY-MM-DD&to=YYYY-MM-DD
    @GetMapping("/{accountId}/daily-summary")
    public List<AccountService.DailySummary> getDailySummary(
        @PathVariable Long accountId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return accountService.getDailySummary(accountId, from, to);
    }
}