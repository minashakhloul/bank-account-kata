package com.bank.account.model;

import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Operation {
    private final String id = UUID.randomUUID().toString();
    private Account account;
    private final Instant date = Instant.now();
    private String description;
    private double amount;
    private OperationType type;

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("type", type)
                .add("date", date)
                .add("amount", amount)
                .toString();
    }
}
