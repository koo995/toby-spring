package spring.toby;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.toby.payment.Payment;
import spring.toby.payment.PaymentService;

import java.io.IOException;

/**
 * 관계 설정의 책임을 Client 가 가져오게 되었다.
 * 필요하다면 SimpleExRateProvider 를 WebApiExRateProvider 로 바꾸기만 하면 된다.
 */
public class Client {
    public static void main(String[] args) throws IOException, InterruptedException {
        /**
         * 이 빈팩토리는 스프링에 있는 빈 팩토리다. 우리 만든 애플리케이션의 구성정보를 모른다.
         * 그 구성정보를 참고하도록 해줘야한다.
         */
        BeanFactory beanFactory = new AnnotationConfigApplicationContext(ObjectFactory.class);
        PaymentService paymentService = beanFactory.getBean(PaymentService.class);
        Payment payment1 = paymentService.prepare(100L, "USD", java.math.BigDecimal.valueOf(50.7));
        System.out.println(payment1);
    }
}

