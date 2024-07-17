package spring.toby;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import spring.toby.data.OrderRepository;
import spring.toby.order.Order;

import java.math.BigDecimal;

/**
 * data 와 관련된 코드를 처리할 client 을 만들어보자.
 */
public class OrderClient {
    public static void main(String[] args) {
        BeanFactory beanFactory = new AnnotationConfigApplicationContext(DataConfig.class);
        OrderRepository orderRepository = beanFactory.getBean(OrderRepository.class);
        /**
         * tx 시작과 커밋을 위한 부분도 템플릿으로 만들기 좋은 코드라서 스프링이 제공해준다.
         * 트랜잭션 템플을 사용하기위해 JpaTransactionManager 를 사용한다.
         */
        JpaTransactionManager transactionManager = beanFactory.getBean(JpaTransactionManager.class);

        try {
            new TransactionTemplate(transactionManager).execute(status -> {
                Order order = new Order(BigDecimal.TEN, "100");
                orderRepository.save(order);
                System.out.println("order = " + order);
                return null;
            });

            /**
             * DataIntegrityViolationException 은 스프링의 예외 추상화를 위한 클래스
             * MyBatis, Jpa, 등등 변경되어도 상관없다.
             */
        } catch (DataIntegrityViolationException e) {
            System.out.println("주문 번호 중복 복구 작업");
        }
    }
}
