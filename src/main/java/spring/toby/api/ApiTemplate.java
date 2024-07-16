package spring.toby.api;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

public class ApiTemplate {

    private final ApiExecutor apiExecutor;
    private final ExRateExtractor exRateExtractor;

    public ApiTemplate() {
        this.apiExecutor = new HttpClientApiExecutor();
        this.exRateExtractor = new ErApiExRateExtractor();
    }


    /**
     * 디폴트 콜백을 내가 원하는 만큼 설정할 수 있다.
     */
    public BigDecimal getExRate(String url) {
        return getExRate(url, this.apiExecutor, this.exRateExtractor);
    }

    public BigDecimal getExRate(String url, ApiExecutor apiExecutor) {
        return getExRate(url, apiExecutor, this.exRateExtractor);
    }

    public BigDecimal getExRate(String url, ExRateExtractor exRateExtractor) {
        return getExRate(url, this.apiExecutor, exRateExtractor);
    }

    public BigDecimal getExRate(String url, ApiExecutor apiExecutor, ExRateExtractor exRateExtractor) {
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
