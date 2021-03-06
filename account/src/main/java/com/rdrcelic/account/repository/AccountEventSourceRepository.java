package com.rdrcelic.account.repository;

import com.rdrcelic.account.model.Account;

import java.time.Instant;

/**
 * This repository holds all events on account object from the beginning of time.
 */
public interface AccountEventSourceRepository {

    void save(Account account);
    Account find(String accountId);

    /**
     * Recreate account with all changes happened before timestamp
     * @param accountId
     * @param timestamp
     * @return
     */
    Account find(String accountId, Instant timestamp);
    void deleteAll();
}
