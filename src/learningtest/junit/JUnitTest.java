package learningtest.junit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/junit.xml")
public class JUnitTest {
    @Autowired
    ApplicationContext context;

    static List<JUnitTest> testObjects = new ArrayList<>();
    static ApplicationContext contextObject = null;

    @Test
    public void test1() {
        Assert.assertThat(testObjects, not(hasItem(this)));
        testObjects.add(this);

        Assert.assertThat(contextObject == null || contextObject == this.context, is(true));
        contextObject = this.context;
    }

    @Test
    public void test2() {
        Assert.assertThat(testObjects, not(hasItem(this)));
        testObjects.add(this);

        Assert.assertTrue(contextObject == null || contextObject == this.context);
        contextObject = this.context;
    }

    @Test
    public void test3() {
        Assert.assertThat(testObjects, not(hasItem(this)));
        testObjects.add(this);

        Assert.assertThat(contextObject, either(is(nullValue())).or(is(this.context)));
        contextObject = this.context;
    }

}
