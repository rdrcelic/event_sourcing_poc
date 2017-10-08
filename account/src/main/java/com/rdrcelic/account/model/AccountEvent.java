package com.rdrcelic.account.model;

import java.time.Instant;

/**
 * It is essential for every account event to know when it occured.
 * Put similar common behaviours here.
 */
public interface AccountEvent {
    Instant occuredAt();
}
