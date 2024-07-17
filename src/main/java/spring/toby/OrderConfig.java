package spring.toby;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import spring.toby.data.OrderRepository;
import spring.toby.order.OrderService;

@Configuration
@Import(DataConfig.class) // 이렇게 다른 설정을 가져올 수 있다.
public class OrderConfig {

    @Bean
    public OrderService orderService() {
        return new OrderService(orderRepository());
    }

    /**
     * DataConfig 에 있던 것을
     * Order 와 관련되어 있으니까 OrderConfig 로 옮겼다.
     */
    @Bean
    public OrderRepository orderRepository() {
        return new OrderRepository();
    }
}
