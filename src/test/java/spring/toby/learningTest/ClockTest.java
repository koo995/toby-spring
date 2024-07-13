package spring.toby.learningTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class ClockTest {
    // Clock을 이용해서 LocalDateTime.now?
    @Test
    void clock() throws Exception {
        Clock clock = Clock.systemDefaultZone();

        /**
         * 계속 바뀌는 시간이라는 것을 검증하기 위함
         */
        LocalDateTime dt1 = LocalDateTime.now(clock);
        LocalDateTime dt2 = LocalDateTime.now(clock);
        Assertions.assertThat(dt2).isAfter(dt1);
    }


    // Clock을 Test에서 사용할 때 내가 원하는 시간을 지정해서 현재 시간을 가져오게 할 수 있는가?
    // 고장난 시계를 하나 선택하고... 그 시간만 고정되도록 설정할 수 있다.
    @Test
    void FixedClock() throws Exception {
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

        /**
         * 두개가 똑같음
         */
        LocalDateTime dt1 = LocalDateTime.now(clock);
        LocalDateTime dt2 = LocalDateTime.now(clock);
        LocalDateTime dt3 = LocalDateTime.now(clock).plusHours(1);
        Assertions.assertThat(dt2).isEqualTo(dt1);
        Assertions.assertThat(dt3).isEqualTo(dt1.plusHours(1));
    }
}
