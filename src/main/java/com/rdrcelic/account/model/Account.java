package com.rdrcelic.account.model;

import com.google.common.collect.ImmutableList;
import io.vavr.API;
import io.vavr.Predicates;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static io.vavr.API.*;
import static io.vavr.Predicates.*;
import static io.vavr.collection.List.ofAll;

@EqualsAndHashCode
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
    private String accountId;

    private List<AccountEvent> newChanges = new ArrayList<>();

    public Account(String accountNumber) {
        this.state = AccountState.BLOCKED;
        accountId = accountNumber;
    }

    public void block() {
        // ACK
        stateChanged(new AccountStateChangedEvent(AccountState.BLOCKED, Instant.now()));
    }

    public void unblock() throws IllegalStateException { // behaviour
        if (isSaldoBiggerThanZero() == false) { // invariant
            throw new IllegalStateException(); // NACK
        }
        // ACK
        stateChanged(new AccountStateChangedEvent(AccountState.UNBLOCKED, Instant.now()));
    }

    public boolean isAccountBlocked() {
        return state != AccountState.UNBLOCKED || !isSaldoBiggerThanZero();
    }

    public void take(BigDecimal amount) throws IllegalStateException { // behaviour
        if (isAccountBlocked()) { // invariant
            throw new IllegalStateException(); // NACK
        }
        // ACK
        amountSubstracted(new AmountSubstractedEvent(amount, Instant.now()));
    }

    public void add(BigDecimal amount) {
        // ACK
        amountAdded(new AmountAddedEvent(amount, Instant.now()));
    }

    public BigDecimal getSaldo() {
        return saldo.setScale(DECIMALS, ROUNDING_MODE);
    }

    public String getAccountId() {
        return accountId;
    }

    public List<AccountEvent> getChanges() {
        return ImmutableList.copyOf(newChanges);
    }

    public void flushChanges() {
        newChanges.clear();
    }

    private Account amountAdded(AmountAddedEvent event) {
        saldo = saldo.add(event.getAmount()); // change state
        newChanges.add(event);

        return this;
    }
    private Account amountSubstracted(AmountSubstractedEvent event) {
        saldo = saldo.subtract(event.getAmount()); // change state
        newChanges.add(event);

        return this;
    }

    private Account stateChanged(AccountStateChangedEvent event) {
        state = event.getState(); // change state
        newChanges.add(event);

        return this;
    }

    private boolean isSaldoBiggerThanZero() {
        if (saldo.compareTo(BigDecimal.ZERO) > 0) {
            return true;
        }

        return false;
    }

    public static Account recreateFrom(String accountId, List<AccountEvent> accountEvents) {
        return ofAll(accountEvents).foldLeft(new Account(accountId), Account::handleEvent);
    }

    private Account handleEvent(AccountEvent accountEvent) {
        // use pattern matching
        return API.Match(accountEvent).of(
                Case($(Predicates.instanceOf(AccountStateChangedEvent.class)), this::stateChanged),
                Case($(Predicates.instanceOf(AmountSubstractedEvent.class)), this::amountSubstracted),
                Case($(Predicates.instanceOf(AmountAddedEvent.class)), this::amountAdded)
        );
    }
}
