package com.bank.account.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class Account {
    private final String id = UUID.randomUUID().toString();
    private String number;
    private Client client;
    private double amount;
    private boolean allowNegativeAmount;
    private double negativeThreshold;
}
