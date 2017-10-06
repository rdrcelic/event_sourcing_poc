package com.rdrcelic.account.repository;

import com.rdrcelic.account.model.Account;

public interface AccountRepository {
    void save(Account account);
    Account find(String accountId);
    void deleteAll();
}
