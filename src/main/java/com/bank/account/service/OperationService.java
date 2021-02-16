package com.bank.account.service;

import com.bank.account.model.Account;
import com.bank.account.model.Operation;
import com.bank.account.repository.InMemoryRepository;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

    private void saveOperation(Operation operation, Account account, double overallAmount) {
        List<Operation> operations = InMemoryRepository.IN_MEM_REPO.get(account);
        if (CollectionUtils.isEmpty(operations)) {
            account.setAmount(overallAmount);
            InMemoryRepository.IN_MEM_REPO.put(account, Lists.newArrayList(operation));
        } else {
            account.setAmount(overallAmount);
            operations.add(operation);
            InMemoryRepository.IN_MEM_REPO.put(account, operations);
        }
    }

    private boolean isOperationAllowed(Account account, double newAmount) {
        return account.isAllowNegativeAmount() || newAmount >= account.getNegativeThreshold();
    }
}
