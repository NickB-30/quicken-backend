package com.nicolas.quicken.repository;

import com.nicolas.quicken.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AccountRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Converts row from SQL query into Account object
    private RowMapper<Account> accountRowMapper = (rs, rowNum) ->
        new Account(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("description")
        );
    
    // Runs SQL query to find all accounts
    public List<Account> findAllAccounts() {
        String sql = "SELECT * FROM accounts";
        return jdbcTemplate.query(sql, accountRowMapper);
    }
}