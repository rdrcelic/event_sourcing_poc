package com.rdrcelic.account.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
public class AmountSubstractedEvent implements AccountEvent {
    private final BigDecimal amount;
    private final Instant when;

    public AmountSubstractedEvent(BigDecimal amount, Instant when) {
        this.amount = amount;
        this.when = when;
    }

    @Override
    public Instant occuredAt() {
        return when;
    }
}
