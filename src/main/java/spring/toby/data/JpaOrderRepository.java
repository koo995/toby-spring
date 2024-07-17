package spring.toby.data;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import spring.toby.order.Order;
import spring.toby.order.OrderRepository;

public class JpaOrderRepository implements OrderRepository {

    /**
     * java 표준에 들어있는 것이다. 엔티티 매니저는 트랜잭션마다 새롭게 만들어져야 한다.
     * 이런 방법으로 가져올 수 있다.
     */
    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(Order order) {
        em.persist(order);
    }
}