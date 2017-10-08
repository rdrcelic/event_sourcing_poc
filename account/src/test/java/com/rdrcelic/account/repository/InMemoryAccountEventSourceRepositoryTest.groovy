package com.rdrcelic.account.repository

import com.rdrcelic.account.model.Account
import spock.lang.Specification

import static org.assertj.core.api.Java6Assertions.assertThat
import static org.assertj.core.api.Java6Assertions.assertThat
import static org.assertj.core.api.Java6Assertions.assertThat

class InMemoryAccountEventSourceRepositoryTest extends Specification {
    AccountEventSourceRepository accountEventSourceRepository = new InMemoryAccountEventSourceRepository();
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
}
