package com.rdrcelic.account.model

import spock.lang.Specification

import static org.assertj.core.api.Assertions.assertThat

class AccountTest extends Specification {

    //EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandom();
    Account account;

    void setup() {
        account = new Account()
    }

    void cleanup() {
    }

    def "cannot take money on blocked account"() {
        given:
            account.block()
        when:
            account.take(BigDecimal.ONE)
        then:
            thrown(IllegalStateException)
    }

    def "cannot unblock account with saldo less or equal to 0"() {
        when:
            account.unblock()
        then:
            thrown(IllegalStateException)
    }

    def "can unblock account with saldo bigger than 0"() {
        given:
            account.add(BigDecimal.TEN)
        when:
            account.unblock()
        then:
            assertThat(account.isAccountBlocked()).isFalse()
    }

    def "account is blocked if saldo is less or equal to 0"() {
        when:
            account.getSaldo() <= BigDecimal.ZERO
        then:
            assertThat(account.isAccountBlocked()).isTrue()
    }

    def "can take money on unblocked account "() {
        given:
            account.add(BigDecimal.TEN)
            account.unblock()
            BigDecimal saldo = account.getSaldo();
            BigDecimal amount = BigDecimal.ONE.setScale(2)
        when:
            account.take(amount)
        then:
            assertThat(account.getSaldo().subtract(saldo).abs()).isEqualTo(amount)
    }

    def "if saldo is less or equal to 0 account turns to blocked"() {
        given:
            account.add(BigDecimal.ONE)
            account.unblock()
        when:
            account.take(BigDecimal.TEN)
        then:
            assertThat(account.isAccountBlocked()).isTrue()
    }

    def "new account is blocked"() {
        given:
            account = new Account();
        expect:
            assertThat(account.isAccountBlocked()).isTrue()
    }

    def "client can add money on blocked account"() {
        given:
            BigDecimal oldSaldo = account.getSaldo();
            BigDecimal amount = BigDecimal.ONE.setScale(2)
        when:
            account.add(amount)
        then:
            BigDecimal newSaldo = account.getSaldo();
            assertThat(newSaldo - oldSaldo).isEqualTo(amount)

    }

    def "client can add money on unblocked account"() {
        given:
            BigDecimal oldSaldo = account.getSaldo();
            BigDecimal amount = BigDecimal.ONE.setScale(2)
        when:
            account.add(amount)
        then:
            BigDecimal newSaldo = account.getSaldo();
            assertThat(newSaldo - oldSaldo).isEqualTo(amount)
    }
}
