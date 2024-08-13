package spring.toby.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static spring.toby.user.UserServiceImpl.MIN_LOG_COUNT_FOR_SILVER;
import static spring.toby.user.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserServiceImpl userServiceImpl;

    private List<User> users;
    @Autowired
    private UserDaoJdbc userDao;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private PlatformTransactionManager transactionManager;

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
        MockUserDao mockUserDao = new MockUserDao(users);
        UserServiceImpl userServiceImpl = new UserServiceImpl(mockUserDao); // 고립된 테스트에서는 테스트 대상 오브젝트를 직접 생성하면 된다.


        // given
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        userServiceImpl.upgradeLevels(); // db 에 저장만 안되었지 업그레이드가 되었는지 안되었는지는 확인이 되구나

        List<User> updated = mockUserDao.getUpdated();
        assertThat(updated.size()).isEqualTo(2);
        checkUserAndLevel(updated.get(0), "joytouch", Level.SILVER);
        checkUserAndLevel(updated.get(1), "madnite1", Level.GOLD);
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
        UserServiceImpl testUserService = new TestUserService(users.get(3).getId(), userDao);
        UserServiceTx txUserService = new UserServiceTx(testUserService, transactionManager);

        // when
        for (User user : users) {
            userDao.add(user);
        }

        try {
            txUserService.upgradeLevels();
//            fail("TestUserServiceException expected");
        } catch (TestUserServiceException e) {
        }
        checkLevelUpgraded(users.get(1), false); // 즉 업그레이드 되었단 말이지
    }

    private void checkUserAndLevel(User updatedUser, String expectedId, Level expectedLevel) {
        assertThat(updatedUser.getId()).isEqualTo(expectedId);
        assertThat(updatedUser.getLevel()).isEqualTo(expectedLevel);
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if (upgraded) {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel().nextLevel());
        } else {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel());
        }
    }

    static class TestUserService extends UserServiceImpl {
        private String id;

        @Autowired
        private TestUserService(String id, UserDao userDao) {
            super(userDao);
            this.id = id;
        }

        protected void upgradeLevel(User user) {
            if (user.getId().equals(id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends RuntimeException {
    }

    static class MockUserDao implements UserDao {
        private List<User> users;
        private List<User> updated = new ArrayList<>();

        private MockUserDao(List<User> users) {
            this.users = users;
        }

        public List<User> getUpdated() {
            return this.updated;
        }

        @Override
        public List<User> getAll() {
            return this.users;
        }

        @Override
        public void update(User user) {
            updated.add(user);
        }

        @Override
        public void add(User user) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAll() {
            throw new UnsupportedOperationException();
        }

        @Override
        public User get(String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getCount() {
            throw new UnsupportedOperationException();
        }
    }
}