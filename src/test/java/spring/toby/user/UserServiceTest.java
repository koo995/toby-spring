package spring.toby.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static spring.toby.user.UserService.MIN_LOG_COUNT_FOR_SILVER;
import static spring.toby.user.UserService.MIN_RECOMMEND_FOR_GOLD;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;
    private List<User> users;
    @Autowired
    private UserDaoJdbc userDao;
    @Autowired
    private DataSource dataSource;

    @BeforeEach
    public void init() {
        users = Arrays.asList(
                new User("bumgin", "박범진", "P1", Level.BASIC, MIN_LOG_COUNT_FOR_SILVER-1, 0),
                new User("joytouch", "강명성", "P2", Level.BASIC, MIN_LOG_COUNT_FOR_SILVER, 0),
                new User("erwins", "신승한", "P3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD-1),
                new User("madnite1", "이상호", "P4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
                new User("green", "오민규", "P5", Level.GOLD, 100, Integer.MAX_VALUE));
    }


    @DisplayName("업그레이드 레벨 테스트")
    @Test
    void upgradeLevels() throws Exception {
        // given
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);
    }

    @DisplayName("사용자 추가 테스트")
    @Test
    public void add() {
        userDao.deleteAll();
        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertThat(userWithLevelRead.getLevel()).isEqualTo(userWithLevel.getLevel());
        assertThat(userWithoutLevelRead.getLevel()).isEqualTo(Level.BASIC);
    }

    @DisplayName("강제 예외 발생을 통한 테스트")
    @Test
    void upgradeAllOrNothing() throws Exception {
        // given
        UserService testUserService = new TestUserService(users.get(3).getId(), userDao, dataSource);
        // when
        for (User user : users) {
            userDao.add(user);
        }

        try {
            testUserService.upgradeLevels();
//            fail("TestUserServiceException expected");
        } catch (TestUserServiceException e) {
        }
        checkLevelUpgraded(users.get(1), false); // 즉 업그레이드 되었단 말이지
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if (upgraded) {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel().nextLevel());
        } else {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel());
        }
    }
    static class TestUserService extends UserService {
        private String id;

        @Autowired
        private TestUserService(String id, UserDao userDao, DataSource dataSource) {
            super(userDao, dataSource);
            this.id = id;
        }

        protected void upgradeLevel(User user) {
            if (user.getId().equals(id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends RuntimeException {
    }
}