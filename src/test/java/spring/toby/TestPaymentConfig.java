package spring.toby;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.toby.payment.ExRateProvider;
import spring.toby.payment.ExRateProviderStub;
import spring.toby.payment.PaymentService;

import java.math.BigDecimal;

@Configuration
//@ComponentScan
public class TestPaymentConfig {

    @Bean
    public PaymentService paymentService() {
        return new PaymentService(exRateProvider()) ;
    }

    @Bean
    public ExRateProvider exRateProvider() {
        return new ExRateProviderStub(BigDecimal.valueOf(1_000));
    }
}
