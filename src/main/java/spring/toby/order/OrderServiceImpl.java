package spring.toby.order;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository ) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(String no, BigDecimal total) {
        Order order = new Order(no, total);

        /**
         * Order 을 리턴해야하는데 람다식이 order 을 리턴하게 만들면
         * execute 라는게 결국 order 을 고대로 리턴하게 된다. 헷갈리면 익명 개체로 바꿔서 확인!
         * 처음에 jpa 로 리포지토리를 만들었을 때는 이 코드를 넣지 않으면 에러가 났다. 트랜잭션을 찾을 수 없다는 것을 jpa 라이브러리가 확인함.
         * 하지만 지금은 제거하여도 에러가 나지 않는다. jdbc 는 기본적으로 오토커밋으로 처리한다.
         */
        orderRepository.save(order);
        return order;
    }

    @Override
    public List<Order> createOrders(List<OrderReq> reqs) {
         return reqs.stream().map(req -> createOrder(req.no(), req.total())).toList();
    }
}
