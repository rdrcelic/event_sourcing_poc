package com.rdrcelic.account.repository;

import com.rdrcelic.account.model.Account;

/**
 * This repository holds raw Account objects. This is not our primary repository for this exercise.
 * For this exercise main repository is {@link AccountEventSourceRepository}.
 */
public interface AccountRepository {
    void save(Account account);
    Account find(String accountId);
    void deleteAll();
}
