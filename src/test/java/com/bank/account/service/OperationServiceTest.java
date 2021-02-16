package com.bank.account.service;


import com.bank.account.model.Account;
import com.bank.account.model.Operation;
import com.bank.account.model.OperationType;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OperationServiceTest {
    @Autowired
    private OperationService operationService;

    @Test
    public void should_add_deposit_operation_to_history() {
        Account account = Account.builder()
                .number("123445LO9")
                .amount(100)
                .build();

        Operation operation = Operation.builder()
                .account(account)
                .description("New deposit")
                .type(OperationType.DEPOSIT)
                .amount(100)
                .build();

        operationService.addOperation(operation);
        assertThat(account.getAmount()).isEqualTo(200);
    }
}