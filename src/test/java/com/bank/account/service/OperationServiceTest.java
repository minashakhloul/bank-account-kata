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
                .allowNegativeAmount(false)
                .negativeThreshold(0)
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

    @Test
    public void should_add_withdraw_operation_to_history() {
        Account account = Account.builder()
                .number("123445LO9")
                .amount(100)
                .allowNegativeAmount(false)
                .negativeThreshold(0)
                .build();

        Operation operation = Operation.builder()
                .account(account)
                .description("New withdraw")
                .type(OperationType.WITHDRAW)
                .amount(60)
                .build();

        operationService.addOperation(operation);
        assertThat(account.getAmount()).isEqualTo(40);
    }

    @Test
    public void should_not_add_withdraw_operation_when_account_is_in_negative() {
        Account account = Account.builder()
                .number("123445LO9")
                .amount(100)
                .allowNegativeAmount(true)
                .negativeThreshold(-200)
                .build();

        Operation operation = Operation.builder()
                .account(account)
                .description("New withdraw")
                .type(OperationType.WITHDRAW)
                .amount(150)
                .build();

        operationService.addOperation(operation);
        assertThat(account.getAmount()).isEqualTo(-50);
    }

    @Test
    public void should_be_able_to_print_account_statement(){
        Account account = Account.builder()
                .number("123445LO9")
                .amount(100)
                .allowNegativeAmount(true)
                .negativeThreshold(-200)
                .build();

        Operation operation1 = Operation.builder()
                .account(account)
                .description("New deposit")
                .type(OperationType.DEPOSIT)
                .amount(500)
                .build();
        operationService.addOperation(operation1);

        Operation operation2 = Operation.builder()
                .account(account)
                .description("New withdraw")
                .type(OperationType.WITHDRAW)
                .amount(150)
                .build();
        operationService.addOperation(operation2);

        String accountStatement = operationService.printAccountStatement(account);
        assertThat(accountStatement).isNotNull();
        assertThat(accountStatement).isNotEmpty();
    }
}