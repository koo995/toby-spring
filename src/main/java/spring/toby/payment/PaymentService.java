package spring.toby.payment;

import org.springframework.stereotype.Service;

import java.io.IOException;
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

    public Payment prepare(Long orderId, String currency, BigDecimal foreignCurrencyAmount) throws IOException {
        // 환율 가져오기
        BigDecimal exRate = exRateProvider.getExRate(currency);
        return Payment.createPrepared(orderId, currency, foreignCurrencyAmount, exRate, LocalDateTime.now (clock));
    }
}
