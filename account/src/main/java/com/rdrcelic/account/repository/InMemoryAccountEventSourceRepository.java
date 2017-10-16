package com.rdrcelic.account.repository;

import com.rdrcelic.account.messaging.EventPublisher;
import com.rdrcelic.account.model.Account;
import com.rdrcelic.account.model.AccountEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class InMemoryAccountEventSourceRepository implements AccountEventSourceRepository {

    private final Map<String, List<AccountEvent>> eventSource = new ConcurrentHashMap<>();

    private final EventPublisher eventPublisher;

    public InMemoryAccountEventSourceRepository(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void save(Account account) {
        List<AccountEvent> newChanges = account.getChanges();
        List<AccountEvent> currentChanges = eventSource.getOrDefault(account.getAccountId(), new ArrayList<>());
        currentChanges.addAll(newChanges);

        eventSource.put(account.getAccountId(), currentChanges);
        newChanges.forEach(eventPublisher::sendEvent);

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
    public Account find(String accountId, Instant timestamp) {
        if (!eventSource.containsKey(accountId)) {
            return null;
        }

        return Account.recreateFrom(
                accountId,
                eventSource.get(accountId).stream().filter(event -> !event.occuredAt().isAfter(timestamp)).collect(Collectors.toList()));
    }

    @Override
    public void deleteAll() {
        eventSource.clear();
    }
}
