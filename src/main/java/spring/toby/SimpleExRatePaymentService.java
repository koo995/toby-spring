package spring.toby;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * 만약에 고정 환율이라면?
 */
public class SimpleExRatePaymentService extends PaymentService{
    @Override
    BigDecimal getExRate(String currency) throws IOException {
        if (currency.equals("USD")) {
            return BigDecimal.valueOf(1000);
        } else {
            throw new IllegalArgumentException("Unsupported currency: " + currency);
        }
    }
}
