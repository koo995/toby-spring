package spring.toby.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

import static spring.toby.user.constant.ConnectionConst.*;

@Configuration
public class DaoFactory {

    @Bean
    public UserDao userDao() {
        return new UserDao(jdbcContext(), dataSource());
    }

    @Bean
    public DataSource dataSource() {
        return new DriverManagerDataSource(URL, USERNAME, PASSWORD);
    }

    @Bean
    public JdbcContext jdbcContext() {
        return new JdbcContext(dataSource());
    }
}

