package spring.toby;

import java.io.IOException;

/**
 * 관계 설정의 책임을 Client 가 가져오게 되었다.
 * 필요하다면 SimpleExRateProvider 를 WebApiExRateProvider 로 바꾸기만 하면 된다.
 */
public class Client {
    public static void main(String[] args) throws IOException {
        ObjectFactory objectFactory = new ObjectFactory();
        PaymentService paymentService = objectFactory.paymentService();
        Payment payment = paymentService.prepare(100L, "USD", java.math.BigDecimal.valueOf(50.7));
        System.out.println(payment);
    }
}

