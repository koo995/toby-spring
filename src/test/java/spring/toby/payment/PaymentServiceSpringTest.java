 package spring.toby.payment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.toby.TestObjectFactory;

import java.time.LocalDateTime;

import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThat;

class PaymentServiceSpringTest {

    @DisplayName("prepare 메소드가 요구사항 3가지를 잘 충족했는지 검증")
    @Test
    void prepare() throws Exception {
        // given
        BeanFactory beanFactory = new AnnotationConfigApplicationContext(TestObjectFactory.class);
        PaymentService paymentService = beanFactory.getBean(PaymentService.class);

        // when
        Payment payment = paymentService.prepare(100L, "USD", TEN);

        // then
        assertThat(payment.getExRate()).isEqualByComparingTo(valueOf(1_000));

        // 완화환산금액 계산
        assertThat(payment.getConvertedAmount()).isEqualTo(valueOf(10_000));

        // 완화환산금액의 유효시간 계산
        // 이것도 검증하기가 만만치 않다..
        assertThat(payment.getValidUntil()).isAfter(LocalDateTime.now());
        assertThat(payment.getValidUntil()).isBefore(LocalDateTime.now().plusMinutes(30));
    }
}