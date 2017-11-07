package com.rdrcelic.account.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rdrcelic.account.json.AccountEventDeserializer;

import java.time.Instant;

/**
 * It is essential for every account event to know when it occured.
 * Put similar common behaviours here.
 */
//@JsonDeserialize(using = AccountEventDeserializer.class)
public interface AccountEvent {
    Instant occuredAt();
}
