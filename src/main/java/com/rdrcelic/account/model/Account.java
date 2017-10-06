package com.rdrcelic.account.model;

import java.math.BigDecimal;

public class Account {
    /**
     * Number of decimals to retain. Also referred to as "scale".
     */
    private static int DECIMALS = 2;
    /**
     * Defined centrally, to allow for easy changes to the rounding mode.
     */
    private static int ROUNDING_MODE = BigDecimal.ROUND_HALF_EVEN;

    private BigDecimal saldo = new BigDecimal("0");
    private AccountState state;

    void block() {
        state = AccountState.BLOCKED;
    }

    void unblock() throws IllegalStateException {
        if (isSaldoBiggerThanZero() == false) {
            throw new IllegalStateException();
        }
        state = AccountState.UNBLOCKED;
    }

    boolean isAccountBlocked() {
        return state != AccountState.UNBLOCKED || !isSaldoBiggerThanZero();
    }

    void take(BigDecimal amount) throws IllegalStateException {
        if (isAccountBlocked()) {
            throw new IllegalStateException();
        }

        saldo = saldo.subtract(amount);
    }

    void add(BigDecimal amount) {
        saldo = saldo.add(amount);
    }

    BigDecimal getSaldo() {
        return saldo.setScale(DECIMALS, ROUNDING_MODE);
    }

    private boolean isSaldoBiggerThanZero() {
        if (saldo.compareTo(BigDecimal.ZERO) > 0) {
            return true;
        }

        return false;
    }
}
