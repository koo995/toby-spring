 package spring.toby.payment;

 import org.junit.jupiter.api.DisplayName;
 import org.junit.jupiter.api.Test;
 import org.junit.jupiter.api.extension.ExtendWith;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.test.context.ContextConfiguration;
 import org.springframework.test.context.junit.jupiter.SpringExtension;
 import spring.toby.TestObjectFactory;

 import java.time.LocalDateTime;

 import static java.math.BigDecimal.TEN;
 import static java.math.BigDecimal.valueOf;
 import static org.assertj.core.api.Assertions.assertThat;

 /**
  * @ContextConfiguration 은 테스트가 실행할때 스프링의 구성정보를 읽은 다음에 이걸 가지고 스프링 컨테이너를 만들고
  * @Autowired 를 통해 의존성 주입을 받을 수 있다.
  * @ExtendWith(SpringExtension.class) 를 사용하면 스프링 테스트 컨텍스트 프레임워크를 junit이 사용할 수 있다. 이것은 기계적으로 기억하자
  */
 @ExtendWith(SpringExtension.class)
 @ContextConfiguration(classes = TestObjectFactory.class)
class PaymentServiceSpringTest {

    @Autowired PaymentService paymentService;
     /**
      * 테스트에 한해서 ExRateProviderStub 을 직접 제어하며 테스트 하고 싶다면 이렇게 하면 된다.
      */
    @Autowired ExRateProviderStub exRateProvider;

    @DisplayName("prepare 메소드가 요구사항 3가지를 잘 충족했는지 검증")
    @Test
    void prepare() throws Exception {

        // exRate: 1000
        Payment payment = paymentService.prepare(100L, "USD", TEN);

        // then
        assertThat(payment.getExRate()).isEqualByComparingTo(valueOf(1_000));

        // 완화환산금액 계산
        assertThat(payment.getConvertedAmount()).isEqualTo(valueOf(10_000));

        // 완화환산금액의 유효시간 계산
        // 이것도 검증하기가 만만치 않다..
        assertThat(payment.getValidUntil()).isAfter(LocalDateTime.now());
        assertThat(payment.getValidUntil()).isBefore(LocalDateTime.now().plusMinutes(30));


        // exRate: 500
        exRateProvider.setExRate(valueOf(500));
        Payment payment2 = paymentService.prepare(100L, "USD", TEN);

        // then
        assertThat(payment2.getExRate()).isEqualByComparingTo(valueOf(500));

        // 완화환산금액 계산
        assertThat(payment2.getConvertedAmount()).isEqualTo(valueOf(5_000));

        // 완화환산금액의 유효시간 계산
        assertThat(payment2.getValidUntil()).isAfter(LocalDateTime.now());
        assertThat(payment2.getValidUntil()).isBefore(LocalDateTime.now().plusMinutes(30));
    }
}