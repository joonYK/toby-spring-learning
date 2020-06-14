package learningtest.template;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class CalcSumTest {

    @Test
    public void sumOfNumbers() throws IOException {
        Calculator calculator = new Calculator();
        int sum = calculator.calcSum(getClass().getResource("numbers.txt").getPath());
        Assert.assertEquals(10, sum);
    }
}
