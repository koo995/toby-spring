package spring.toby.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserDaoTest {

    @Autowired
    private ApplicationContext ac;

    @BeforeEach
    void init() throws SQLException {
        UserDao dao = ac.getBean("userDao", UserDao.class);
        dao.deleteAll();
    }

    @AfterEach
    void tearDown() throws SQLException {
        UserDao dao = ac.getBean("userDao", UserDao.class);
        dao.deleteAll();
    }

    @DisplayName("Add and Get 테스트")
    @Test
    void addAndGetTest() throws Exception {
        // given
        UserDao dao = ac.getBean("userDao", UserDao.class);
        User user1 = new User("whiteship", "백기선", "married");
        User user2 = new User("whiteship2", "백기선2", "married2");


        dao.add(user1);

        User findUser = dao.get(user1.getId());

        assertThat(findUser.getId()).isEqualTo(user1.getId());
        assertThat(findUser.getName()).isEqualTo(user1.getName());
    }

    @DisplayName("카운트 테스트")
    @Test
    void countTest() throws Exception {
        // given
        UserDao dao = ac.getBean("userDao", UserDao.class);

        User user1 = new User("whiteship1", "백기선1", "married1");
        User user2 = new User("whiteship2", "백기선2", "married2");
        User user3 = new User("whiteship3", "백기선3", "married3");

        dao.add(user1);
        dao.add(user2);
        dao.add(user3);

        assertThat(dao.getCount()).isEqualTo(3);
    }

    @DisplayName("Get 실패 테스트")
    @Test
    void getFail() throws Exception {
        // given
        UserDao dao = ac.getBean("userDao", UserDao.class);
        assertThat(dao.getCount()).isEqualTo(0);

        // when
        assertThrows(EmptyResultDataAccessException.class, () -> {
            dao.get("unknown_id");
        });

    }

}