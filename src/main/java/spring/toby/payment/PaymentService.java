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

        // 환율 가져오기 by httpie
        BigDecimal exRate = exRateProvider.getExRate(currency);

        // 금액 계산
        BigDecimal convertedAmount = foreignCurrencyAmount.multiply(exRate);

        // 유효 시간 계산
        LocalDateTime validUntil = LocalDateTime.now(clock).plusMinutes(30);

        return new Payment(orderId, currency, foreignCurrencyAmount, exRate, convertedAmount, validUntil);
    }
}
