package spring.toby.payment;

import java.math.BigDecimal;

/**
 * 무엇을 리턴해줄까?
 * 중요한 것은 테스트 코드에서 얘가 리턴하는 값이 뭐다 라는 것 미리 알고있는 채로 사용할 수 있게 해주는 것
 */
public class ExRateProviderStub implements ExRateProvider {

    public BigDecimal getExRate() {
        return exRate;
    }

    public void setExRate(BigDecimal exRate) {
        this.exRate = exRate;
    }

    private BigDecimal exRate;

    public ExRateProviderStub(BigDecimal exRate) {
        this.exRate = exRate;
    }

    @Override
    public BigDecimal getExRate(String currency) {
        return exRate;
    }
}
