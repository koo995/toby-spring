package spring.toby;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.toby.order.Order;

import java.math.BigDecimal;

/**
 * data 와 관련된 코드를 처리할 client 을 만들어보자.
 */
public class DataClient {
    public static void main(String[] args) {
        BeanFactory beanFactory = new AnnotationConfigApplicationContext(DataConfig.class);
        EntityManagerFactory emf = beanFactory.getBean(EntityManagerFactory.class);

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Order order = new Order(BigDecimal.TEN, "100");
        em.persist(order);

        System.out.println("order = " + order);

        em.getTransaction().commit();
        em.close();
    }
}
