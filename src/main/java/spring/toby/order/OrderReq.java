package spring.toby.order;

import java.math.BigDecimal;

public record OrderReq(String no, BigDecimal total) {
}
