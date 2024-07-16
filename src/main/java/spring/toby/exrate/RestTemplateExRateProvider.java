package spring.toby.exrate;

import org.springframework.web.client.RestTemplate;
import spring.toby.payment.ExRateProvider;

import java.math.BigDecimal;

public class RestTemplateExRateProvider implements ExRateProvider {

    private final RestTemplate restTemplate;

    public RestTemplateExRateProvider(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public BigDecimal getExRate(String currency) {
        String url = "https://open.er-api.com/v6/latest/" + currency;
        /**
         * ExRateData 클래스로 메시지 컨버터가 전환해서 돌려주는게 가능하다.
         */
        return restTemplate.getForObject(url, ExRateData.class).rates().get("KRW");
    }
}
