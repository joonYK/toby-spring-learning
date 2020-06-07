package learningtest.junit;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;

public class JUnitTest {
    static List<JUnitTest> testObjects = new ArrayList<>();

    @Test
    public void test1() {
        Assert.assertThat(testObjects, not(hasItem(this)));
        testObjects.add(this);
    }

    @Test
    public void test2() {
        Assert.assertThat(testObjects, not(hasItem(this)));
        testObjects.add(this);
    }

    @Test
    public void test3() {
        Assert.assertThat(testObjects, not(hasItem(this)));
        testObjects.add(this);
    }

}
