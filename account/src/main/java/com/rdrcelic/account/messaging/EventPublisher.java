package com.rdrcelic.account.messaging;

import com.rdrcelic.account.events.AccountEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.Publisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventPublisher {

    @Publisher(channel = AccountSource.OUTPOUT_CHANNEL)
    public AccountEvent sendEvent(AccountEvent event) {
        log.info("sending event: " + event);
        return event;
    }
}
