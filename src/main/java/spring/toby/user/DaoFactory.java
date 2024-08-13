package spring.toby.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

import static spring.toby.user.constant.ConnectionConst.*;

@Configuration
public class DaoFactory {

    @Bean
    public UserDaoJdbc userDao() {
        return new UserDaoJdbc(dataSource());
    }

    @Bean
    public DataSource dataSource() {
        return new DriverManagerDataSource(URL, USERNAME, PASSWORD);
    }

    @Bean
    public UserService userServiceImpl() {
        return new UserServiceImpl(userDao());
    }

    @Bean
    public TxProxyFactoryBean userService() {
        return new TxProxyFactoryBean(userServiceImpl(), transactionManager(), "upgradeLevels", UserService.class);
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}

