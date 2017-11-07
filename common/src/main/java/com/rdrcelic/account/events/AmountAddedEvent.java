package com.rdrcelic.account.events;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@NoArgsConstructor
public class AmountAddedEvent implements AccountEvent {
    private BigDecimal amount;
    private Instant when;

    public AmountAddedEvent(BigDecimal amount, Instant when) {
        this.amount = amount;
        this.when = when;
    }

    @Override
    public Instant occuredAt() {
        return when;
    }
}
