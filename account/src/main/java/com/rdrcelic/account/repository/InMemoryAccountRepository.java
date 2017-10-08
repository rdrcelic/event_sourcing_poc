package com.rdrcelic.account.repository;

import com.rdrcelic.account.model.Account;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAccountRepository implements AccountRepository {
    private final Map<String, Account> memoryDataSource = new ConcurrentHashMap<>();

    @Override
    public void save(Account account) {
        memoryDataSource.put(account.getAccountId(), account);
    }

    @Override
    public Account find(String accountId) {
        return memoryDataSource.get(accountId);
    }

    @Override
    public void deleteAll() {
        memoryDataSource.clear();
    }
}
