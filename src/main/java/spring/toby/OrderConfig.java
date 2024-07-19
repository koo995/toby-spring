package spring.toby;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import spring.toby.data.JdbcOrderRepository;
import spring.toby.order.OrderRepository;
import spring.toby.order.OrderService;
import spring.toby.order.OrderServiceImpl;

import javax.sql.DataSource;

@Configuration
@Import(DataConfig.class) // 이렇게 다른 설정을 가져올 수 있다.
@EnableTransactionManagement // 트랜잭션 관리 기능을 하도록 선언할 수 있다. 프록시 만드는 것을 스프링이 해야함을 알려줌
public class OrderConfig {

    /**
     * txManager 는 같은 클래스에서 정의한 것은 아니니까
     * 빈 팩토리 메서드에 파라미터로 전달받을 수 있다.
     */
    @Bean
    public OrderService orderService(OrderRepository orderRepository) {
        return new OrderServiceImpl(orderRepository);
    }

    /**
     * DataConfig 에 있던 것을
     * Order 와 관련되어 있으니까 OrderConfig 로 옮겼다.
     */
    @Bean
    public OrderRepository orderRepository(DataSource dataSource) {
        return new JdbcOrderRepository(dataSource);
    }
}
