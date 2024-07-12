package spring.toby;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class ObjectFactory {

//    @Bean
//    public PaymentService paymentService() {
//        return new PaymentService(exRateProvider()) ;
//    }
//
//    /**
//     * 이렇게 분리하면 뭐가 좋냐?
//     * 나중에 ExRateProvider 를 바꾸고 싶다면
//     * 메서드를 통해 빨리 찾아갈 수 있다.
//     */
//    @Bean
//    public ExRateProvider exRateProvider() {
//        return new WebApiExRateProvider();
//    }
}
