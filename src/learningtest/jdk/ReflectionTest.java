package learningtest.jdk;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

public class ReflectionTest {

    @Test
    public void invokeMethod() throws Exception {
        String name = "String";

        Assert.assertEquals(name.length(), 6);

        Method lengthMethod = String.class.getMethod("length");
        Assert.assertEquals((int)lengthMethod.invoke(name), 6);

        Assert.assertEquals(name.charAt(0), 'S');

        Method charAtMethod = String.class.getMethod("charAt", int.class);
        Assert.assertEquals((char)charAtMethod.invoke(name, 0), 'S');

    }
}
