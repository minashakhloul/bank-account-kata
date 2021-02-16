package com.bank.account.repository;

import com.bank.account.model.Account;
import com.bank.account.model.SingleStatement;
import com.google.common.collect.Maps;

import java.util.LinkedList;
import java.util.Map;

public interface InMemoryRepository {
    Map<Account, LinkedList<SingleStatement>> IN_MEM_REPO = Maps.newHashMap();
}
