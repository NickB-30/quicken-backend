package com.nicolas.quicken.repository;

import com.nicolas.quicken.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

@Repository
public class TransactionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Converts row from SQL query into Transaction object
    private RowMapper<Transaction> transactionRowMapper = (rs, rowNum) ->
        new Transaction(
            rs.getLong("id"),
            rs.getLong("account_id"),
            rs.getObject("date", LocalDate.class),
            rs.getBigDecimal("amount"),
            rs.getString("description")
        );
    
    // Runs SQL query to find transactions filtered by account and date range
    public List<Transaction> findByAccountIdAndDateRange(Long accountId, LocalDate fromDate, LocalDate toDate) {
        String sql = "SELECT * FROM transactions WHERE account_id = ? AND date BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, transactionRowMapper, accountId, fromDate, toDate);
    }
}