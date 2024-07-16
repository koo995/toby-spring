package spring.toby.exrate;

import spring.toby.api.ApiTemplate;
import spring.toby.payment.ExRateProvider;

import java.math.BigDecimal;

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
        /**
         * 이런 생각을 해볼 수 있다. 이거는 환율 정보를 가져오는 그런 기능을 담당하는 구현인데, 여기다가 매번 이렇게 어떤 ApiExecutor 의 구현 클래스에
         * 어떤 기술을 사용한다는 것을 알고 노출시켜야할까? 이런 생각을 할 수 있다.
         * 매번 특정 기술의 콜백 기억하고 있다가 뭘 사용할지 결정하고 넣어줘야할까? 필요하다면 넣을 수 있지만... 이 두가지는 ApiTemplate 에서 기본적으로 제공해주는
         * 디폴트 값으로 지정해주면 더 좋지 않을까? 이런 생각을 할 수 있다.
         */
        return apiTemplate.getExRate(url);
    }
}
