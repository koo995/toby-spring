package spring.toby.order;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import spring.toby.OrderConfig;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 평범한 테스트가 아니라 스프링을 이용하는 테스트라는 것을 애너테이션으로 알려줘야한다.
 * junit 이 인식해서 스프링을 띄운다.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = OrderConfig.class)
public class OrderServiceSpringTest {
    @Autowired
    OrderService orderService;

    @Autowired
    DataSource dataSource;

    @Test
    void createOrder() {
        Order order = orderService.createOrder("0100", BigDecimal.ONE);

        assertThat(order.getId()).isGreaterThan(0);
    }

    @Test
    void createOrders() {
        List<OrderReq> orderReqs = List.of(new OrderReq("0200", BigDecimal.ONE),
                new OrderReq("0201", BigDecimal.TWO));
        List<Order> orders = orderService.createOrders(orderReqs);
        assertThat(orders).hasSize(2);
        orders.forEach(order -> assertThat(order.getId()).isGreaterThan(0));
    }

    /**
     * 이전까지 별 문제없어 보일 수 있다. 그러나 트랜잭션이 하나하나 안걸려있다는 것을 검증할 필요가 있다.
     * 요구사항에 따르면 주문 번호가 중복되면 테스트가 실패해야한다.
     * 우리가 검증하고 싶은 것은 첫번째 것은 성공적으로 들어갔고, 두번째 것을 넣다가 DataIntegrityViolationException 이 발생하여
     * 전체적으로 실패를 하였는데, 그 경우에 앞에 넣은 order 도 롤백이 되어 db에 넣은 작업이 취소되도록 tx가 동작하는지 테스트하고 싶다.
     * 즉 createOrders 가 하나의 tx로 동작하는지 확인하고 싶다.
     */
    @Test
    void createDuplicateOrders() {
        List<OrderReq> orderReqs = List.of(new OrderReq("0300", BigDecimal.ONE),
                new OrderReq("0300", BigDecimal.TWO));
        assertThatThrownBy(() -> orderService.createOrders(orderReqs))
                .isInstanceOf(DataIntegrityViolationException.class);

        JdbcClient client = JdbcClient.create(dataSource);
        Integer count = client.sql("select count(*) from orders where no = '0300'")
                .query(Integer.class)
                .single();
        /**
         * 하나의 tx로 동작함을 볼수있다.
         */
        assertThat(count).isEqualTo(0);
    }
}
