package spring.toby.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserDaoTest {

    @Autowired
    private ApplicationContext ac;

    @AfterEach
    void tearDown() throws SQLException {
        UserDao dao = ac.getBean("userDao", UserDao.class);
        dao.deleteAll();
        assertThat(dao.getCount()).isEqualTo(0);
    }

    @DisplayName("Add and Get 테스트")
    @Test
    void addAndGetTest() throws Exception {
        // given
        UserDao dao = ac.getBean("userDao", UserDao.class);
        User user1 = new User();
        user1.setId("whiteship");
        user1.setName("백기선");
        user1.setPassword("married");

        dao.add(user1);

        User user2 = dao.get(user1.getId());

        assertThat(user2.getId()).isEqualTo(user1.getId());
        assertThat(user2.getName()).isEqualTo(user1.getName());


        // when

        // then
    }

}