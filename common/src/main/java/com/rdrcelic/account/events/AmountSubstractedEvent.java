package com.rdrcelic.account.events;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@NoArgsConstructor
public class AmountSubstractedEvent implements AccountEvent {
    private BigDecimal amount;
    private Instant when;

    public AmountSubstractedEvent(BigDecimal amount, Instant when) {
        this.amount = amount;
        this.when = when;
    }

    @Override
    public Instant occuredAt() {
        return when;
    }
}
