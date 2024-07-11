package spring.toby;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * 만약에 고정 환율이라면?
 */
public class SimpleExRateProvider  implements ExRateProvider{

    @Override
    public BigDecimal getExRate(String currency) throws IOException {
        if (currency.equals("USD")) {
            return BigDecimal.valueOf(1000);
        } else {
            throw new IllegalArgumentException("Unsupported currency: " + currency);
        }
    }
}
