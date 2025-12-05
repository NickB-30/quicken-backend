package com.nicolas.quicken.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    // Account fields
    private Long id;
    private String name;
    private String description;
}