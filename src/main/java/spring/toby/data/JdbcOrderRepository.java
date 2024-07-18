package spring.toby.data;

import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.simple.JdbcClient;
import spring.toby.order.Order;
import spring.toby.order.OrderRepository;

import javax.sql.DataSource;

public class JdbcOrderRepository implements OrderRepository {

    private final JdbcClient jdbcClient;

    public JdbcOrderRepository(DataSource dataSource) {
        this.jdbcClient = JdbcClient.create(dataSource);
    }

    @PostConstruct
    void init() {
        jdbcClient.sql("""
                create table orders (id bigint not null, no varchar(255), total numeric(38,2), primary key (id));
                alter table if exists orders drop constraint if exists UK43egxxciqr9ncgmxbdx2avi8n;
                alter table if exists orders add constraint UK43egxxciqr9ncgmxbdx2avi8n unique (no);
                create sequence orders_SEQ start with 1 increment by 50;
                """).update();
    }

    /**
     * jpa 는 db 테이블을 자동으로 만들어주지만, jdbc 는 자동으로 만들어주지 않는다.
     */
    @Override
    public void save(Order order) {
        /**
         * 하나 까다로운 것은 sequence 로부터 id값을 jpa는 조회해서 그 값을 가지 db에 넣어줘야한다.
         * 팁? 새로운 기술을 사용할때 잘 동작하는지 한단계 한단계 문제없이 동작하는지 찍어보면 좋다
         */
        Long id = jdbcClient.sql("select next value for orders_SEQ;").query(Long.class).single();// list 가 아니라 하나의 데이터로 가져오니까 single
        System.out.println("id = " + id);

        /**
         * jpa 는 리플렉션을 이용해서 id 값을 넣어주지만
         * 여기서 불편하게 리플렉션을 이용하는 것보다 그냥 setter 을 열어준다.
         * params 의 순서는 되게 중요하다!
         */
        order.setId(id);
        jdbcClient.sql("insert into orders (no,total,id) values (?,?,?);")
                .params(order.getNo(), order.getTotal(), order.getId())
                .update();

    }
}
