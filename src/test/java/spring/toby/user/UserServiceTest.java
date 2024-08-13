package spring.toby.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;
import static spring.toby.user.UserServiceImpl.MIN_LOG_COUNT_FOR_SILVER;
import static spring.toby.user.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService; // TxProxyFactoryBean 에서 생성하는 다이내믹 프록시를 통해 UserService 기능을 사용하게 될 것이다.
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


    // mock 오브젝트를 이용하니까 트랜잭션과는 무관하다.
    @DisplayName("업그레이드 레벨 테스트")
    @Test
    void upgradeLevels() throws Exception {
        UserDao mockUserDao = Mockito.mock(UserDao.class);
        when(mockUserDao.getAll()).thenReturn(users);

        UserServiceImpl userServiceImpl = new UserServiceImpl(mockUserDao); // 고립된 테스트에서는 테스트 대상 오브젝트를 직접 생성하면 된다.

        userServiceImpl.upgradeLevels(); // db 에 저장만 안되었지 업그레이드가 되었는지 안되었는지는 확인이 되구나

        verify(mockUserDao, times(2)).update(any(User.class)); // update 메소드가 두 번 호출됐는지 확인
        verify(mockUserDao, times(2)).update(any(User.class)); // update 메소드가 두 번 호출됐는지 확인
        verify(mockUserDao).update(users.get(1)); // 두 번째 호출된 파라미터가 users.get(1)인지 확인
        assertThat(users.get(1).getLevel()).isEqualTo(Level.SILVER);
        verify(mockUserDao).update(users.get(3)); // 네 번째 호출된 파라미터가 users.get(3)인지 확인
        assertThat(users.get(3).getLevel()).isEqualTo(Level.GOLD);
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

    /**
     * 여기서... testUserService 을 이용해야 하는데... 팩토리 빈을 불변으로 설정했으니 바꾸기가 어렵네...
     * 이런 경우를 대비해서 setter 주입을 쓰기도 하는 구나
     */
    @DisplayName("강제 예외 발생을 통한 테스트")
    @Test
    void upgradeAllOrNothing() throws Exception {
        // given
        UserServiceImpl testUserService = new TestUserService(users.get(3).getId(), userDao);
        TransactionHandler txHandler = new TransactionHandler(testUserService, transactionManager, "upgradeLevels");

        UserService txUserService = (UserService) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{UserService.class}, txHandler);

        // when
        for (User user : users) {
            userDao.add(user);
        }

        try {
            txUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        } catch (TestUserServiceException e) {
        }
        checkLevelUpgraded(users.get(1), false); // 즉 업그레이드가 실패되었단 말이지
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