package spring.toby.exrate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import spring.toby.payment.ExRateProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Collectors;

public class WebApiExRateProvider implements ExRateProvider {

    /**
     * @throws IOException 은 우리가 제어할 수 없는 외부의 문제이다. 인터넷을 끊어졌거나... api 제공 업체에 장애가 나타났거나..
     * 진짜 예외적인 상황이다.
     * 캐치를 해가지고 무언가를 처리하지 않으면 안되는 상황.
     * 반드시 예외적인 상황이다라는 것을 시스템의 앞단으로 던져줘야 한다.
     * runtime exception 을 던지면 된다.
     * 예외를 캐치해가지고 뭔가를 처리하는 이유는 복구하기 위함.
     * 그러나 그 상황을 복구할만한 설계가 없다면 무시하면 된다.
     */
    @Override
    public BigDecimal getExRate(String currency) {
        String url = "https://open.er-api.com/v6/latest/";

        URI uri;
        try {
            uri = new URI(url + currency);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        String response;
        try {
            HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
            /**
             * close 을 위해서 finally 을 사용해야하는데
             * java 에서 try-with-resources 라는 기능을 제공한다.
             */
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                response = br.lines().collect(Collectors.joining());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ExRateData data;
        try {
            ObjectMapper mapper = new ObjectMapper();
            data = mapper.readValue(response, ExRateData.class);
            return data.rates().get("KRW");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
