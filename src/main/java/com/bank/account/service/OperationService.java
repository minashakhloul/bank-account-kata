package com.bank.account.service;

import com.bank.account.model.Account;
import com.bank.account.model.Operation;
import com.bank.account.model.OperationType;
import com.bank.account.model.SingleStatement;
import com.bank.account.repository.InMemoryRepository;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;

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

        if (!isOperationAllowed(account, overallAmount, operation.getType())) {
            throw new RuntimeException(String.format("Unauthorized operation of %s on account : %s", newAmount, account.getNumber()));
        }

        saveOperation(operation, account, overallAmount);
    }

    public String printAccountStatement(Account account) {
        LinkedList<SingleStatement> accountStatements = InMemoryRepository.IN_MEM_REPO.get(account);
        if (CollectionUtils.isEmpty(accountStatements)) {
            throw new RuntimeException(String.format("No statements available, unknown account: %s", account.getNumber()));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("OPERATION | DATE | AMOUNT | BALANCE")
                .append("\n");
        accountStatements.forEach(singleStatement ->
                sb.append(singleStatement.getOperation().getType())
                        .append(" | ")
                        .append(singleStatement.getOperation().getDate())
                        .append(" | ")
                        .append(singleStatement.getOperation().getAmount())
                        .append(" | ")
                        .append(singleStatement.getBalance())
                        .append("\n"));
        return sb.toString();
    }

    public LinkedList<SingleStatement> getAccountStatement(Account account) {
        LinkedList<SingleStatement> accountStatements = InMemoryRepository.IN_MEM_REPO.get(account);
        if (CollectionUtils.isEmpty(accountStatements)) {
            throw new RuntimeException(String.format("No statements available for account: %s", account.getNumber()));
        }
        return accountStatements;
    }

    private void saveOperation(Operation operation, Account account, double overallAmount) {
        LinkedList<SingleStatement> accountStatements = InMemoryRepository.IN_MEM_REPO.get(account);
        SingleStatement statement = SingleStatement.builder()
                .operation(operation)
                .balance(overallAmount)
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

    private boolean isOperationAllowed(Account account, double newAmount, OperationType operationType) {
        switch (operationType) {
            case DEPOSIT:
                return newAmount <= account.getPositiveThreshold();
            case WITHDRAW:
                return account.isAllowNegativeAmount() || newAmount >= account.getNegativeThreshold();
            default:
                return false;
        }
    }
}
