package spring.toby.exrate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 레코드의 주의할 점은 한번 값을 넘은 다음 수정할 수 없다.
 * 우리는 json 에서 필요한 것만 뽑아서 사용할 것이니, 생성자에 없는 것들은 무시하자
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ExRateData(String result, Map<String, BigDecimal> rates) {
}
