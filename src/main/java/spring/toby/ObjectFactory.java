package spring.toby;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.toby.exrate.CachedExRateProvider;
import spring.toby.exrate.ExRateProvider;
import spring.toby.exrate.WebApiExRateProvider;
import spring.toby.payment.PaymentService;

@Configuration
//@ComponentScan
public class ObjectFactory {

    @Bean
    public PaymentService paymentService() {
        return new PaymentService(cachedExRateProvider()) ;
    }

    @Bean
    public ExRateProvider cachedExRateProvider() {
        return new CachedExRateProvider(exRateProvider());
    }

    /**
     * 이렇게 분리하면 뭐가 좋냐?
     * 나중에 ExRateProvider 를 바꾸고 싶다면
     * 메서드를 통해 빨리 찾아갈 수 있다.
     */
    @Bean
    public ExRateProvider exRateProvider() {
        return new WebApiExRateProvider();
    }
}
