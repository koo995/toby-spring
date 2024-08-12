package spring.toby.calculator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CalculatorTest {
    @DisplayName("더하기 테스트")
    @Test
    void sumOfNumbers() throws Exception {
        // given
        Calculator calculator = new Calculator();
        String numFilepath = getClass().getResource("/numbers.txt").getPath();
        int sum = calculator.calcSum(numFilepath);
        Assertions.assertThat(sum).isEqualTo(10);
    }
}