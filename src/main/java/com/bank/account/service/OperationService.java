package com.bank.account.service;

import com.bank.account.model.Account;
import com.bank.account.model.Operation;
import com.bank.account.model.SingleStatement;
import com.bank.account.repository.InMemoryRepository;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

@Service
public class OperationService {

    public void addOperation(Operation operation) {
        Account account = operation.getAccount();
        double newAmount;
        switch (operation.getType()) {
            case DEPOSIT:
                newAmount = operation.getAmount();
                break;
            case WITHDRAW:
                newAmount = operation.getAmount() * -1;
                break;
            default:
                throw new IllegalArgumentException("Unknown operation type <" + operation.getType() + ">");
        }
        double overallAmount = account.getAmount() + newAmount;

        if (!isOperationAllowed(account, overallAmount)) {
            throw new RuntimeException(String.format("Unauthorized operation of %s on account : %s", newAmount, account.getNumber()));
        }

        saveOperation(operation, account, overallAmount);
    }

    public String printAccountStatement(Account account){
       return null;
    }

    private void saveOperation(Operation operation, Account account, double overallAmount) {
        LinkedList<SingleStatement> accountStatements = InMemoryRepository.IN_MEM_REPO.get(account);
        SingleStatement statement = SingleStatement.builder()
                .operation(operation)
                .currentBalance(account.getAmount())
                .build();
        if (CollectionUtils.isEmpty(accountStatements)) {
            account.setAmount(overallAmount);
            LinkedList<SingleStatement> statements = Lists.newLinkedList();
            statements.add(statement);
            InMemoryRepository.IN_MEM_REPO.put(account, statements);
        } else {
            account.setAmount(overallAmount);
            accountStatements.add(statement);
            InMemoryRepository.IN_MEM_REPO.put(account, accountStatements);
        }
    }

    private boolean isOperationAllowed(Account account, double newAmount) {
        return account.isAllowNegativeAmount() || newAmount >= account.getNegativeThreshold();
    }
}
