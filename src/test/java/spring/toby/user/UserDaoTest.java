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

    @Autowired
    private UserDao userDao;

    @BeforeEach
    void before() throws SQLException {
        userDao.deleteAll();
    }

    @AfterEach
    void after() throws SQLException {
        userDao.deleteAll();
    }

    @DisplayName("Add and Get 테스트")
    @Test
    void addAndGetTest() throws Exception {
        // given
        User user1 = new User("whiteship", "백기선", "married");
        User user2 = new User("whiteship2", "백기선2", "married2");


        userDao.add(user1);

        User findUser = userDao.get(user1.getId());

        assertThat(findUser.getId()).isEqualTo(user1.getId());
        assertThat(findUser.getName()).isEqualTo(user1.getName());
    }

    @DisplayName("카운트 테스트")
    @Test
    void countTest() throws Exception {
        // given
        User user1 = new User("whiteship1", "백기선1", "married1");
        User user2 = new User("whiteship2", "백기선2", "married2");
        User user3 = new User("whiteship3", "백기선3", "married3");

        userDao.add(user1);
        userDao.add(user2);
        userDao.add(user3);

        assertThat(userDao.getCount()).isEqualTo(3);
    }

    @DisplayName("Get 실패 테스트")
    @Test
    void getFail() throws Exception {
        // given
        assertThat(userDao.getCount()).isEqualTo(0);

        // when
        assertThrows(EmptyResultDataAccessException.class, () -> {
            userDao.get("unknown_id");
        });
    }
}