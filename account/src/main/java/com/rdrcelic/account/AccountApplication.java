package com.rdrcelic.account;

import com.rdrcelic.account.messaging.AccountSource;
import com.rdrcelic.account.model.Account;
import com.rdrcelic.account.repository.AccountEventSourceRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.util.UUID;

@EnableScheduling
@EnableBinding(AccountSource.class)
@SpringBootApplication
public class AccountApplication {

    private final AccountEventSourceRepository eventSourceRepository;

    public AccountApplication(AccountEventSourceRepository eventSourceRepository) {
        this.eventSourceRepository = eventSourceRepository;
    }

    public static void main(String[] args) {
		SpringApplication.run(AccountApplication.class, args);
	}

	@Scheduled(fixedRate = 2000L)
	public void createRandomAccount() {
        Account account = new Account(UUID.randomUUID().toString());

        account.add(BigDecimal.TEN.setScale(2));
        eventSourceRepository.save(account);
    }
}
