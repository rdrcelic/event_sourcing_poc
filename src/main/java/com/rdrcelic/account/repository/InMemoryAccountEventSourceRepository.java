package com.rdrcelic.account.repository;

import com.rdrcelic.account.model.Account;
import com.rdrcelic.account.model.AccountEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAccountEventSourceRepository implements AccountEventSourceRepository {

    private Map<String, List<AccountEvent>> eventSource = new ConcurrentHashMap<>();

    @Override
    public void save(Account account) {
        List<AccountEvent> newChanges = account.getChanges();
        List<AccountEvent> currentChanges = eventSource.getOrDefault(account.getAccountId(), new ArrayList<>());
        currentChanges.addAll(newChanges);

        eventSource.put(account.getAccountId(), currentChanges);
        account.flushChanges();
    }

    @Override
    public Account find(String accountId) {
        if (!eventSource.containsKey(accountId)) {
            return null;
        }
        return Account.recreateFrom(accountId, eventSource.get(accountId));
    }

    @Override
    public void deleteAll() {
        eventSource.clear();
    }
}
