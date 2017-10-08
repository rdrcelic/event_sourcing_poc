package com.rdrcelic.account.model;

import lombok.Getter;

import java.time.Instant;

@Getter
public class AccountStateChangedEvent implements AccountEvent {
    private final AccountState state;
    private final Instant when;

    public AccountStateChangedEvent(AccountState state, Instant when) {
        this.state = state;
        this.when = when;
    }

    @Override
    public Instant occuredAt() {
        return when;
    }
}
