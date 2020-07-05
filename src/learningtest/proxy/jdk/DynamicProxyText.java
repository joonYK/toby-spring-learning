package learningtest.proxy.jdk;

import learningtest.proxy.Hello;
import learningtest.proxy.HelloTarget;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxyText {

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

    @Test
    public void simpleProxy() {
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] {Hello.class},
                new UppercaseHandler(new HelloTarget())
        );

        Assert.assertEquals(proxiedHello.sayHello("JY"), "HELLO JY");
        Assert.assertEquals(proxiedHello.sayHi("JY"), "HI JY");
        Assert.assertEquals(proxiedHello.sayThankYou("JY"), "THANK YOU JY");
    }

}
