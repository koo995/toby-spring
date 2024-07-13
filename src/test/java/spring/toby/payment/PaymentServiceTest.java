package spring.toby.payment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spring.toby.exrate.WebApiExRateProvider;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentServiceTest {

    @DisplayName("prepare 메소드가 요구사항 3가지를 잘 충족했는지 검증")
    @Test
    void prepare() throws Exception {
        // given
        PaymentService paymentService = new PaymentService(new WebApiExRateProvider());

        // when
        Payment payment = paymentService.prepare(100L, "USD", BigDecimal.TEN);

        // then
        // 환율정보 가져온다. 다만 정확한 값을 알 방법이 없다. isEqualTo 을 쓰긴 어렵다.
        // 가장 쉬운 방법은... 있는지 없는지?
        assertThat(payment.getExRate()).isNotNull();

        // 완화환산금액 계산
        assertThat(payment.getConvertedAmount())
                .isEqualTo(payment.getExRate().multiply(payment.getForeignCurrencyAmount()));

        // 완화환산금액의 유효시간 계산
        // 이것도 검증하기가 만만치 않다..
        assertThat(payment.getValidUntil()).isAfter(LocalDateTime.now());
        assertThat(payment.getValidUntil()).isBefore(LocalDateTime.now().plusMinutes(30));

    }

}