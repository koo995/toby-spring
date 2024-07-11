package spring.toby;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class PaymentService {
    public Payment prepare(Long orderId, String currency, BigDecimal foreignCurrencyAmount) throws IOException {
        /**
         * 환율 가져오기 by httpie
         * http -v https://open.er-api.com/v6/latest/USD
         * 이용하면 터미널에서 가능
         */
        URL url = new URL("https://open.er-api.com/v6/latest/" + currency);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        /**
         * inputStream 으로 받은 리턴 값은 사람이 알아볼 수 없다.
         * java 문자로 변경하기위해 InputStreamReader를 사용한다.
         * 사람이 알아볼 수 있게 하기 위해 BufferedReader를 사용한다.
         * 이런 것은 실무에서 꽤 자주 볼 수 있다.
         */
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        /**
         * java8 이후로 추가된 메소드
         * lines() 메소드는 BufferedReader의 모든 라인을 Stream으로 반환한다.
         */
        String response = br.lines().collect(Collectors.joining());
        br.close();

        ObjectMapper mapper = new ObjectMapper();
        ExchangeData data = mapper.readValue(response, ExchangeData.class);
        BigDecimal exRate = data.rates().get("KRW");

        // 금액 계산
        BigDecimal convertedAmount = foreignCurrencyAmount.multiply(exRate);

        // 유효 시간 계산
        LocalDateTime validUntil = LocalDateTime.now().plusMinutes(30);

        return new Payment(orderId, currency, foreignCurrencyAmount, exRate, convertedAmount, validUntil);
    }

    public static void main(String[] args) throws IOException {
        PaymentService paymentService = new PaymentService();
        Payment payment = paymentService.prepare(100L, "USD", BigDecimal.valueOf(50.7));
        System.out.println(payment);
    }
}
