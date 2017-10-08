package com.rdrcelic.account.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
public class AmountAddedEvent implements AccountEvent {
    private final BigDecimal amount;
    private final Instant when;

    public AmountAddedEvent(BigDecimal amount, Instant when) {
        this.amount = amount;
        this.when = when;
    }

    @Override
    public Instant occuredAt() {
        return when;
    }
}
