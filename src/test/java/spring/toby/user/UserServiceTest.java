package spring.toby.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;
    private List<User> users;
    @Autowired
    private UserDaoJdbc userDao;

    @BeforeEach
    public void init() {
        users = Arrays.asList(
                new User("bumgin", "박범진", "P1", Level.BASIC, 49, 0),
                new User("joytouch", "강명성", "P2", Level.BASIC, 50, 0),
                new User("erwins", "신승한", "P3", Level.SILVER, 60, 29),
                new User("madnite1", "이상호", "P4", Level.SILVER, 60, 30),
                new User("green", "오민규", "P5", Level.GOLD, 100, 100));
    }


    @DisplayName("업그레이드 레벨 테스트")
    @Test
    void upgradeLevelTest() throws Exception {
        // given
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        userService.upgradeLevels();

        checkLevel(users.get(0), Level.BASIC);
        checkLevel(users.get(1), Level.SILVER);
        checkLevel(users.get(2), Level.SILVER);
        checkLevel(users.get(3), Level.GOLD);
        checkLevel(users.get(4), Level.GOLD);
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


    private void checkLevel(User user, Level expectedLevel) {
        User userUpdate = userDao.get(user.getId());
        assertThat(userUpdate.getLevel()).isEqualTo(expectedLevel);
    }

}