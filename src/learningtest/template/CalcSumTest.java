package learningtest.template;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class CalcSumTest {

    Calculator calculator;
    String numFilepath;

    @Before
    public void setUp() {
        this.calculator = new Calculator();
        this.numFilepath = getClass().getResource("numbers.txt").getPath();
    }

    @Test
    public void sumOfNumbers() throws IOException {
        Assert.assertEquals(calculator.calcSum(numFilepath), 10);
    }

    @Test
    public void multiplyOfNumbers() throws IOException {
        Assert.assertEquals(calculator.calcMultiply(numFilepath), 24);
    }

    @Test
    public void concatenateOfNumbers() throws IOException {
        Assert.assertEquals(calculator.concatenate(numFilepath), "1234");
    }

}
