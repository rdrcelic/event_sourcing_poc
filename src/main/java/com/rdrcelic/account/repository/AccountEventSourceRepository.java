package com.rdrcelic.account.repository;

import com.rdrcelic.account.model.Account;
import com.rdrcelic.account.model.AccountEvent;

import java.util.List;

public interface AccountEventSourceRepository {

    void save(Account account);
    Account find(String accountId);
    void deleteAll();
}
