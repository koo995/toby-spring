package spring.toby.payment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThat;

class PaymentServiceTest {

    Clock clock;

    @BeforeEach
    void beforeEach() {
        this.clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
    }


    @DisplayName("prepare 메소드가 요구사항 3가지를 잘 충족했는지 검증")
    @Test
    void prepare() throws Exception {
        // given
        PaymentService paymentService = new PaymentService(new ExRateProviderStub(valueOf(500)), this.clock);

        // when
        Payment payment = paymentService.prepare(100L, "USD", TEN);

        // then
        /**
         * 환율정보 가져온다.
         * BigDecimal 을 이용한 비교를 할땐 isEqualByComparingTo 를 사용하자.
         */
        assertThat(payment.getExRate()).isEqualByComparingTo(valueOf(500));

        // 완화환산금액 계산
        assertThat(payment.getConvertedAmount()).isEqualTo(valueOf(5_000));

        // 완화환산금액의 유효시간 계산
        // 이것도 검증하기가 만만치 않다..
        assertThat(payment.getValidUntil()).isAfter(LocalDateTime.now());
        assertThat(payment.getValidUntil()).isBefore(LocalDateTime.now().plusMinutes(30));
    }

    @DisplayName("유효시간이 30분 뒤인지 검증")
    @Test
    void validUntil () throws Exception {
        // given
        PaymentService paymentService = new PaymentService(new ExRateProviderStub(valueOf(1000)), this.clock);

        // when
        Payment payment = paymentService.prepare(100L, "USD", TEN);

        // then
        LocalDateTime now = LocalDateTime.now(this.clock);
        LocalDateTime expectedValidUntil = now.plusMinutes(30);
        assertThat(payment.getValidUntil()).isEqualTo(expectedValidUntil);
    }
}