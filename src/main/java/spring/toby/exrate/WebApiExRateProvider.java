package spring.toby.exrate;

import spring.toby.api.ApiTemplate;
import spring.toby.api.ErApiExRateExtractor;
import spring.toby.payment.ExRateProvider;

import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WebApiExRateProvider implements ExRateProvider {

    /**
     * 템플릿을 만들어서 분리하였다.
     * 템플릿은 그 안에 상태값을 가지고 계속 변경되는 것이 아니고 서버에서 동작한다고 한다 하더라도
     * 멀티스레드에서 안전하므로 클래스의 인스턴스가 만들어질때 딱 하나만 만들어지도록하고 재사용하도록한다.
     */
    ApiTemplate apiTemplate = new ApiTemplate();

    @Override
    public BigDecimal getExRate(String currency) {
        String url = "https://open.er-api.com/v6/latest/" + currency;
        return apiTemplate.getExRate(url, uri -> {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();
            try(HttpClient client = HttpClient.newBuilder().build()) {
                return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } ,new ErApiExRateExtractor());
    }
}
