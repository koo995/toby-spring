package spring.toby;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.orm.jpa.JpaTransactionManager;
import spring.toby.order.Order;
import spring.toby.order.OrderService;

import java.math.BigDecimal;

/**
 * data 와 관련된 코드를 처리할 client 을 만들어보자.
 */
public class OrderClient {
    public static void main(String[] args) {
        BeanFactory beanFactory = new AnnotationConfigApplicationContext(OrderConfig.class);
        OrderService service = beanFactory.getBean(OrderService.class);
        JpaTransactionManager transactionManager = beanFactory.getBean(JpaTransactionManager.class);

        Order order = service.createOrder("0100", BigDecimal.TEN);
        System.out.println(order);
    }
}
