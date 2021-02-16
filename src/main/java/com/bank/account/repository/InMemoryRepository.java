package com.bank.account.repository;

import com.bank.account.model.Account;
import com.bank.account.model.Operation;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public interface InMemoryRepository {
    Map<Account, List<Operation>> IN_MEM_REPO = Maps.newHashMap();
}
