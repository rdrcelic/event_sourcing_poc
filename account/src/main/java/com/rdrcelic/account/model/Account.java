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
import static io.vavr.collection.List.ofAll;

/**
 * Account object is a product of <a href="https://techbeacon.com/introduction-event-storming-easy-way-achieve-domain-driven-design">event storming</a>.
 * We tried to recognise domain events, like in this case "account state changed" (from blocked to un-blocked), or "amound added".
 *
 * Design started with listing "account behaviours" - no properties known at first. We recognised:
 * - block()
 * - unblock()
 * - isAccountBlocked()
 * - take()
 * - add()
 * - getSaldo()
 * Some behaviours consider some other invariants (like "you cannot take money from 'blocked' account).
 * At this stage it is easier to recognise events to describe account object.
 * In final step instead of changing account object in behaviour method, we fire appropriate event.
 * For the sake of event sourcing pattern, these events have cause two things:
 * 1) object state changes
 * 2) event has been stored in local "event store" - newChanges
 * Attention - at the time of account object creation, newChanges is empty. During the lifetime of account instance
 * all new changes are tracked down in newChanges event store. Eventually, what is persisted is "the list of all events"
 * so the mechanism should append newChanges to the history of changes in DataStore and after persisted process should flush
 * newChanges making it ready for new changes.
 *
 * Account class knows how to create an account instance by re-creating from "event source" DataStore.
 * At that stage, all events are going to be applied on the object from the beginning of time in exact same order.
 *
 */

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

    public void block() { // bahaviour
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

    /**
     * Utility method re-creates Account by applying all events from the beginning of time in the same order.
     * Here is "functional style" from <a href="http://www.vavr.io/vavr-docs/">vavr library</a> used.
     * In essence, on new Account object we are calling the Account::handleEvent method.
     * @param accountId
     * @param accountEvents
     * @return
     */
    public static Account recreateFrom(String accountId, List<AccountEvent> accountEvents) {
        return ofAll(accountEvents).foldLeft(new Account(accountId), Account::handleEvent);
    }

    /**
     * This handler method has to know all Account events and how each of them impact the state of Account object.
     * To avoid commond if/then, or switch construct, more elegant pattern matching is used from <a href="http://www.vavr.io/vavr-docs/">vavr library</a>.
     * @param accountEvent
     * @return
     */
    private Account handleEvent(AccountEvent accountEvent) {
        // use pattern matching
        return API.Match(accountEvent).of(
                Case($(Predicates.instanceOf(AccountStateChangedEvent.class)), this::stateChanged),
                Case($(Predicates.instanceOf(AmountSubstractedEvent.class)), this::amountSubstracted),
                Case($(Predicates.instanceOf(AmountAddedEvent.class)), this::amountAdded)
        );
    }
}
