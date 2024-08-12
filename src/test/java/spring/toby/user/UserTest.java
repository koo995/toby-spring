package spring.toby.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {
    User user;

    @BeforeEach
    void init() {
        user = new User();
    }

    @DisplayName("유저 업게르이드 테스트")
    @Test
    void upgradeLevel() throws Exception {
        // given
        Level[] levels = Level.values();
        for (Level level : levels) {
            if (level.nextLevel() == null) {
                continue;
            }
            user.setLevel(level);
            // when
            user.upgradeLevel();
            // then
            assertThat(user.getLevel()).isEqualTo(level.nextLevel());
        }
    }

    @DisplayName("유저 업그레이드 불가능 테스트")
    @Test
    void cannotUpgradeLevel() throws Exception {
        // given
        Level[] levels = Level.values();
        for (Level level : levels) {
            if (level.nextLevel() != null) {
                continue;
            }
            user.setLevel(level);
            assertThatThrownBy(() -> user.upgradeLevel())
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining(level + "은 업그레이드가 불가능합니다.");
        }
    }

}