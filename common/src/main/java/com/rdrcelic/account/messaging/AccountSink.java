package com.rdrcelic.account.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * Interface describes input channel for account events
 */
public interface AccountSink {
    String INPUT_CHANNEL = "accountInputChannel";

    @Input(INPUT_CHANNEL)
    SubscribableChannel accountChangeChannel();
}
