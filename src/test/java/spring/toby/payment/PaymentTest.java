package spring.toby.payment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 우리가 테스트를 만들 때 막 계속 신경 썼던 환율정보 가져오고 clock을 뭘 쓰고
 * 그런것이 payment에 대해서만 test을 만들면 이런 고민이 사라진다
 * 왜냐면 payment 는 그냥 얘가 뭔가 정보를 의존하는 그런게 아니라 간단하게 페이먼트를 준비하는 동안에 필요로 하는 어떤 작업만 딱 수행하는 놈이니까
 * 테스트가 간단해 진다.
 * 환율정보가 어떻고 그런거는 paymentService 에서 신경쓰면 된다.
 */
class PaymentTest {

    @DisplayName("payment 테스트")
    @Test
    void createPrepared() throws Exception {
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        Payment payment = Payment.createPrepared(
                1L, "USD", BigDecimal.TEN, BigDecimal.valueOf(1000), LocalDateTime.now(clock)
        );

        assertThat(payment.getConvertedAmount()).isEqualByComparingTo(BigDecimal.valueOf(10_000));
        assertThat(payment.getValidUntil()).isEqualTo(LocalDateTime.now(clock).plusMinutes(30));
    }

    @DisplayName("payment 유효성 검증")
    @Test
    void isValid() {
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        Payment payment = Payment.createPrepared(
                1L, "USD", BigDecimal.TEN, BigDecimal.valueOf(1000), LocalDateTime.now(clock)
        );

        assertThat(payment.isValid(clock)).isTrue();
    }

    @DisplayName("payment 유효성 30분 검증")
    @Test
    void isValid2() {
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        Payment payment = Payment.createPrepared(
                1L, "USD", BigDecimal.TEN, BigDecimal.valueOf(1000), LocalDateTime.now(clock)
        );

        assertThat(payment.isValid(clock)).isTrue();
        assertThat(payment.isValid(Clock.offset(clock, Duration.of(30, ChronoUnit.MINUTES)))).isFalse();
    }

}