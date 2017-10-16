package com.rdrcelic.account.repository

import com.rdrcelic.account.messaging.EventPublisher
import com.rdrcelic.account.model.Account
import org.mockito.Mock
import org.springframework.boot.test.mock.mockito.MockBean
import spock.lang.Specification

import java.time.Instant

import static org.assertj.core.api.Java6Assertions.assertThat

class InMemoryAccountEventSourceRepositoryTest extends Specification {

    EventPublisher eventPublisher = new EventPublisher();

    AccountEventSourceRepository accountEventSourceRepository = new InMemoryAccountEventSourceRepository(eventPublisher);
    Account account

    void setup() {
        account = new Account(UUID.randomUUID().toString())
    }

    void cleanup() {
        accountEventSourceRepository.deleteAll()
    }

    def "repository can persist account and find it after accountId"() {
        given:
            String accountId = account.getAccountId()
        when:
            accountEventSourceRepository.save(account)
        and:
            Account account1 = accountEventSourceRepository.find(accountId)
        then:
            assertThat(account).isEqualTo(account1)
    }

    def "repository can update account"() {
        given:
            String accountId = account.getAccountId()
            accountEventSourceRepository.save(account)
        and:
            BigDecimal amount = BigDecimal.TEN.setScale(2)
            account.add(amount)
        when:
            accountEventSourceRepository.save(account)
        and:
            account = accountEventSourceRepository.find(accountId)
        then:
            assertThat(account.getSaldo()).isEqualTo(amount)
        and:
            assertThat(account.getAccountId()).isEqualTo(accountId)
    }

    def "repository can find account after accountId with latest changes"() {
        given:
            String accountId = account.getAccountId()
        when:
            accountEventSourceRepository.save(account)
        and:
            Account account1 = accountEventSourceRepository.find(accountId)
            account1.add(BigDecimal.TEN.setScale(2))
        and:
            account1.unblock();
            account1.take(BigDecimal.ONE.setScale(2))
            sleep(500L)
            Instant referencePoint = Instant.now();
            account1.take(BigDecimal.ONE.setScale(2))
            account1.take(BigDecimal.ONE.setScale(2))
            account1.take(BigDecimal.ONE.setScale(2))
        and:
            accountEventSourceRepository.save(account1)
        then:
            Account account2 = accountEventSourceRepository.find(accountId, referencePoint.minusNanos(1))
            assertThat(account.getAccountId()).isEqualTo(account2.getAccountId())
            assertThat(account2.getSaldo()).isEqualTo(new BigDecimal("9").setScale(2));
    }
}
