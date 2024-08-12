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

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void before() throws SQLException {
        System.out.println("ac = " + ac);
        System.out.println("this = " + this);
        userDao.deleteAll();
        this.user1 = new User("gyumee", "박성철", "springno1", Level.BASIC, 1, 0);
        this.user2 = new User("leegw700", "이길원", "springno2", Level.SILVER, 55, 10);
        this.user3 = new User("bumjin", "박범진", "springno3", Level.GOLD, 100, 40);
    }

    @AfterEach
    void after() throws SQLException {
        userDao.deleteAll();
    }

    @DisplayName("Add and Get 테스트")
    @Test
    void addAndGetTest() throws Exception {
        userDao.add(user1);
        userDao.add(user2);
        userDao.add(user3);

        User findUser = userDao.get(user1.getId());
        checkSameUser(user1, findUser);

        findUser = userDao.get(user2.getId());
        checkSameUser(user2, findUser);

        findUser = userDao.get(user3.getId());
        checkSameUser(user3, findUser);
    }

    @DisplayName("카운트 테스트")
    @Test
    void countTest() throws Exception {
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
        userDao.add(user1);
        List<User> users1 = userDao.getAll();
        assertThat(users1.size()).isEqualTo(1);
        checkSameUser(user1, users1.get(0));

        userDao.add(user2);
        List<User> users2 = userDao.getAll();
        assertThat(users2.size()).isEqualTo(2);
        checkSameUser(user1, users2.get(0));
        checkSameUser(user2, users2.get(1));

        userDao.add(user3);
        List<User> users3 = userDao.getAll();
        assertThat(users3.size()).isEqualTo(3);
        checkSameUser(user1, users3.get(1));
        checkSameUser(user2, users3.get(2));
        checkSameUser(user3, users3.get(0));
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
        assertThat(user1.getLevel()).isEqualTo(user2.getLevel());
        assertThat(user1.getLogin()).isEqualTo(user2.getLogin());
        assertThat(user1.getRecommend()).isEqualTo(user2.getRecommend());
    }

    @DisplayName("사용자 정보 수정 메소드 테스트")
    @Test
    void update() throws Exception {
        // given
        userDao.add(user1);
        userDao.add(user2);
        // when

        user1.setName("오민규");
        user1.setPassword("springno6");
        user1.setLevel(Level.GOLD);
        user1.setLogin(1000);
        user1.setRecommend(999);
        userDao.update(user1);

        // then
        User user1Update = userDao.get(user1.getId());
        checkSameUser(user1, user1Update);
        User user2Same = userDao.get(user2.getId());
        checkSameUser(user2, user2Same);
    }
}