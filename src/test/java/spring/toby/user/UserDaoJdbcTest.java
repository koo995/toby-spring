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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserDaoJdbcTest {

    @Autowired
    private ApplicationContext ac;

    @Autowired
    private UserDao userDao;

    @BeforeEach
    void before() throws SQLException {
        System.out.println("ac = " + ac);
        System.out.println("this = " + this);
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

    @DisplayName("전체 가져옴 테스트")
    @Test
    void getAll() throws Exception {
        // given
        User user1 = new User("whiteship1", "백기선1", "married1");
        userDao.add(user1);
        List<User> users1 = userDao.getAll();
        assertThat(users1.size()).isEqualTo(1);
        checkSameUser(user1, users1.get(0));

        User user2 = new User("whiteship2", "백기선2", "married2");
        userDao.add(user2);
        List<User> users2 = userDao.getAll();
        assertThat(users2.size()).isEqualTo(2);
        checkSameUser(user1, users2.get(0));
        checkSameUser(user2, users2.get(1));

        User user3 = new User("whiteship3", "백기선3", "married3");
        userDao.add(user3);
        List<User> users3 = userDao.getAll();
        assertThat(users3.size()).isEqualTo(3);
        checkSameUser(user1, users3.get(0));
        checkSameUser(user2, users3.get(1));
        checkSameUser(user3, users3.get(2));
    }

    @DisplayName("데이터가 없을 때의 getAll 테스트")
    @Test
    void getAll2() throws Exception {
        List<User> users = userDao.getAll();
        assertThat(users.size()).isEqualTo(0);
    }

    private void checkSameUser(User user1, User user2) {
        assertThat(user1.getId()).isEqualTo(user2.getId());
        assertThat(user1.getName()).isEqualTo(user2.getName());
        assertThat(user1.getPassword()).isEqualTo(user2.getPassword());
    }
}