package spring.toby;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.toby.data.OrderRepository;
import spring.toby.order.Order;

import java.math.BigDecimal;

/**
 * data 와 관련된 코드를 처리할 client 을 만들어보자.
 */
public class DataClient {
    public static void main(String[] args) {
        BeanFactory beanFactory = new AnnotationConfigApplicationContext(DataConfig.class);
        OrderRepository orderRepository = beanFactory.getBean(OrderRepository.class);

        Order order = new Order(BigDecimal.TEN, "100");
        orderRepository.save(order);
        System.out.println("order = " + order);
    }
}
