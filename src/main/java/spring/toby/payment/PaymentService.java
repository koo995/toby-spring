package spring.toby.payment;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final ExRateProvider exRateProvider;
    /**
     * 시계의 종류에 따라서 현재 시간을 어느 지역에 어떤 방식으로 동작하 시계로부터 가져온다를 결정할 수 있다.
     */
    private final Clock clock;

    public PaymentService(ExRateProvider exRateProvider, Clock clock) {
        this.exRateProvider = exRateProvider;
        this.clock = clock;
    }

    /**
     * 이 paymentService 가 이용하는 ExRateProvider 라 인터페이스에는 어디에도 네트워크를 이용해서
     * api을 호출해서 결과를 받아온다. 그런 것이 없다.
     * ExRateProvider 인터페이스 차원에서 보, ioException을 꼭 던져야 되는 것처럼 이 메소드를 정의해 놓는다는 것은 이상한 것이다.
     * simpleRxRateProvider 는 예외를 던질 필요가 없지만, 정의되어 있다. 즉 이것은 이상하니 없애는게 맞다.
     * ExRateProvider 환율 정보를 가져오는 어떤 프로바이더다 라는 입장에서 ioException(체크 예외)을 던지도록 선언하는 것은 잘못된 설계이다.
     */
    public Payment prepare(Long orderId, String currency, BigDecimal foreignCurrencyAmount) {
        // 환율 가져오기
        BigDecimal exRate = exRateProvider.getExRate(currency);
        return Payment.createPrepared(orderId, currency, foreignCurrencyAmount, exRate, LocalDateTime.now (clock));
    }
}
