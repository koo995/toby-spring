package spring.toby;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;
import spring.toby.data.JdbcOrderRepository;
import spring.toby.order.OrderRepository;
import spring.toby.order.OrderService;

import javax.sql.DataSource;

@Configuration
@Import(DataConfig.class) // 이렇게 다른 설정을 가져올 수 있다.
public class OrderConfig {

    /**
     * txManager 는 같은 클래스에서 정의한 것은 아니니까
     * 빈 팩토리 메서드에 파라미터로 전달받을 수 있다.
     */
    @Bean
    public OrderService orderService(PlatformTransactionManager transactionManager,
                                     DataSource dataSource) {
        return new OrderService(orderRepository(dataSource), transactionManager);
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
