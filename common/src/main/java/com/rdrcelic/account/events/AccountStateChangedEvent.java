package com.rdrcelic.account.events;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
public class AccountStateChangedEvent implements AccountEvent {
    private AccountState state;
    private Instant when;

    public AccountStateChangedEvent(AccountState state, Instant when) {
        this.state = state;
        this.when = when;
    }

    @Override
    public Instant occuredAt() {
        return when;
    }
}
