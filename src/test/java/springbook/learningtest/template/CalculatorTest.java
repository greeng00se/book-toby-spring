package springbook.learningtest.template;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class CalculatorTest {

    Calculator calculator;
    String numFilePath;

    @BeforeEach
    public void setUp() {
        this.calculator = new Calculator();
        this.numFilePath = getClass().getResource("/numbers.txt").getPath();
    }
    @Test
    void sumOfNumbers() throws IOException {
        Assertions.assertThat(calculator.calcSum(this.numFilePath)).isEqualTo(10);
    }

    @Test
    void multiplyOfNumbers() throws IOException {
        Assertions.assertThat(calculator.calcMultiply(this.numFilePath)).isEqualTo(24);
    }

    @Test
    void concatenateStrings() throws IOException {
        Assertions.assertThat(calculator.concatenate(this.numFilePath)).isEqualTo("1234");
    }
}