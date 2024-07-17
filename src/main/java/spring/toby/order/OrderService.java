package spring.toby.order;

import org.springframework.stereotype.Service;
import spring.toby.data.OrderRepository;

import java.math.BigDecimal;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(String no, BigDecimal total) {
        Order order = new Order(total, no);
        this.orderRepository.save(order);
        return null;
    }
}
