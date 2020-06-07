package learningtest.junit;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

public class JUnitTest {
    static JUnitTest testObject;

    @Test
    public void test1() {
        Assert.assertThat(this, is(not(sameInstance(testObject))));
        testObject = this;
    }

    @Test
    public void test2() {
        Assert.assertThat(this, is(not(sameInstance(testObject))));
        testObject = this;
    }

    @Test
    public void test3() {
        Assert.assertThat(this, is(not(sameInstance(testObject))));
        testObject = this;
    }

}
