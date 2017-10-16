package com.rdrcelic.account.messaging;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * Interface describes output channel for account events
 */
public interface AccountSource {
    String OUTPOUT_CHANNEL = "account_channel";

    @Output(AccountSource.OUTPOUT_CHANNEL)
    MessageChannel accountChangeChannel();
}
