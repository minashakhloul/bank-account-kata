package com.bank.account.model;

import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class SingleStatement {
    private Operation operation;
    private double currentBalance;

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("operation", operation)
                .add("balance", currentBalance)
                .toString();
    }
}
