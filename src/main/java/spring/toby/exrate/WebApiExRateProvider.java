package spring.toby.exrate;

import com.fasterxml.jackson.core.JsonProcessingException;
import spring.toby.api.ApiExecutor;
import spring.toby.api.ErApiExRateExtractor;
import spring.toby.api.ExRateExtractor;
import spring.toby.api.SimpleApiExecutor;
import spring.toby.payment.ExRateProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

public class WebApiExRateProvider implements ExRateProvider {

    /**
     * 템플릿의 메서드를 호출해주는 이 코드가 클라이언트가 된다.
     */
    @Override
    public BigDecimal getExRate(String currency) {
        String url = "https://open.er-api.com/v6/latest/" + currency;
        return runApiForExRate(url, new SimpleApiExecutor(),new ErApiExRateExtractor());
    }

    /**
     * 인터페이스 타입으로 정의한다.
     * 실행해야하는 시점에 인터페이스 타입에 어떤 클래스의 오브젝트인지 몰라야한다.
     * 그래서 어디서 주입하냐?
     * 우리가 작성하는 코드에 의해서. SimpleApiExecutor 이 바로 콜백이다. 변경이 필요할 때, 이 클래스를 변경하면 된다. 클래스를 정의하지 않고 람다를 사용해도 된다.
     * 이 콜백을 받아서 내부에서 수행하는 runApiForExRate 이 템플릿이다.
     */
    private static BigDecimal runApiForExRate(String url, ApiExecutor apiExecutor, ExRateExtractor exRateExtractor) {
        URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        String response;
        try {
            response = apiExecutor.execute(uri);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            return exRateExtractor.extract(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
