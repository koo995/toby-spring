package spring.toby.calculator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CalculatorTest {
    Calculator calculator;
    String numFilepath;

    @BeforeEach
    public void init() {
        this.calculator = new Calculator();
        this.numFilepath = getClass().getResource("/numbers.txt").getPath();
    }


    @DisplayName("더하기 테스트")
    @Test
    void sumOfNumbers() throws Exception {
        Assertions.assertThat(calculator.calcSum(this.numFilepath)).isEqualTo(10);
    }
}