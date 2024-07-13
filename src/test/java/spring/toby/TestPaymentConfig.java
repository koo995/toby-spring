package spring.toby;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.toby.payment.ExRateProvider;
import spring.toby.payment.ExRateProviderStub;
import spring.toby.payment.PaymentService;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

@Configuration
//@ComponentScan
public class TestPaymentConfig {

    @Bean
    public PaymentService paymentService() {
        return new PaymentService(exRateProvider(), clock()) ;
    }

    @Bean
    public ExRateProvider exRateProvider() {
        return new ExRateProviderStub(BigDecimal.valueOf(1_000));
    }

    /**
     * 테스트를 위해선 고정된 시간이어야 한다.
     */
    @Bean
    public Clock clock() {
        return Clock.fixed(Instant.now(), ZoneId.systemDefault());
    }
}
