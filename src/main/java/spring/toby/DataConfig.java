package spring.toby;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import spring.toby.data.OrderRepository;

import javax.sql.DataSource;

@Configuration
public class DataConfig {
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource());
        emf.setPackagesToScan("spring.toby");
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter() {{
                setDatabase(Database.H2);
                setGenerateDdl(true);
                setShowSql(true);
        }});
        return emf;
    }

    /**
     * PersistenceContext 애너테이션을 후러치해주는 빈 후처리기의 기능이 필요하다.
     * 되게 기술적인거지만.. 이해하는데 지금은 너무 시간을쓰지말자?
     */
    @Bean
    public BeanPostProcessor persistenceAnnotationbeanPostProcessor() {
        return new PersistenceAnnotationBeanPostProcessor();
    }

    /**
     * 트랜잭션 매니저도 필요하다.
     * jpa을 사용하면서 트랜잭션을 사용할 때는 jpa 트랜잭션 매니저를 사용해야한다.
     */
    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }


    @Bean
    public OrderRepository orderRepository() {
        return new OrderRepository();
    }
}
