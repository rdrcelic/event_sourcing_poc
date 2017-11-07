package com.rdrcelic.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rdrcelic.account.events.AccountEvent;
import com.rdrcelic.account.events.AccountStateChangedEvent;
import com.rdrcelic.account.events.AmountAddedEvent;
import com.rdrcelic.account.events.AmountSubstractedEvent;
import com.rdrcelic.account.messaging.AccountSink;
import io.vavr.Predicates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import static io.vavr.API.*;

@Slf4j
@SpringBootApplication
@EnableBinding(AccountSink.class)
public class ClientApplication {

    @Autowired
    ObjectMapper objectMapper;

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

    @StreamListener(AccountSink.INPUT_CHANNEL)
    public void handleAccountEvent(AccountEvent event) {

        // use pattern matching to dispatch events to appropriate event handlers
        Match(event).of(
                Case($(Predicates.instanceOf(AccountStateChangedEvent.class)), this::stateChanged),
                Case($(Predicates.instanceOf(AmountSubstractedEvent.class)), this::amountSubstracted),
                Case($(Predicates.instanceOf(AmountAddedEvent.class)), this::amountAdded)
        );
    }

    private AccountStateChangedEvent stateChanged(AccountStateChangedEvent event) {
        log.debug("received event: " + event);
        return event;
    }

    private AmountSubstractedEvent amountSubstracted(AmountSubstractedEvent event) {
        log.debug("received event: " + event);
        return event;
    }

    private AmountAddedEvent amountAdded(AmountAddedEvent event) {
        log.debug("received event: " + event);
        return event;
    }
}
