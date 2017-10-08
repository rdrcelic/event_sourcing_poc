package com.rdrcelic.account.repository

import com.rdrcelic.account.AccountApplication
import com.rdrcelic.account.model.Account
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import static org.assertj.core.api.Java6Assertions.assertThat

class InMemoryAccountRepositoryTest extends Specification {

    AccountRepository accountRepository = new InMemoryAccountRepository();
    Account account

    void setup() {
        account = new Account(UUID.randomUUID().toString())
    }

    void cleanup() {
        accountRepository.deleteAll()
    }

    def "repository can persist account and find it after accountId"() {
        given:
            String accountId = account.getAccountId()
        when:
            accountRepository.save(account)
        and:
            Account account1 = accountRepository.find(accountId)
        then:
            assertThat(account).isEqualTo(account1)
    }

    def "repository can update account"() {
        given:
            String accountId = account.getAccountId()
            accountRepository.save(account)
        and:
            BigDecimal amount = BigDecimal.TEN.setScale(2)
            account.add(amount)
        when:
            accountRepository.save(account)
        and:
            Account account1 = accountRepository.find(accountId)
        then:
            assertThat(account).isEqualTo(account1)
        and:
            assertThat(account.getSaldo()).isEqualTo(amount)
    }
}
