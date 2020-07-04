package learningtest.jdk.proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

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

    /**
     * 스프링의 프록시 기술을 추상화한 proxyFactoryBean 학습 테스트
     */
    @Test
    public void proxyFactoryBean() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());

        //부가기능을 담은 어드바이스 추가. 여러개 추가 가능.
        pfBean.addAdvice(new UppercaseAdvice());

        Hello proxiedHello = (Hello) pfBean.getObject();
        Assert.assertEquals(proxiedHello.sayHello("JY"), "HELLO JY");
        Assert.assertEquals(proxiedHello.sayHi("JY"), "HI JY");
        Assert.assertEquals(proxiedHello.sayThankYou("JY"), "THANK YOU JY");

    }

    @Test
    public void pointcutAdvisor() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());

        //메소드 이름을 비교해서 대상을 선정하는 알고리즘을 제공하는 포인트컷
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        //sayH로 시작하는 메서드 이름의 메서드에 부가기능을 적용
        pointcut.setMappedName("sayH*");

        //포인트컷(부가기능 부여 선정기준)과 어드바이스(부가기능)를 Advisor로 묶어서 한 번에 추가
        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

        Hello proxiedHello = (Hello) pfBean.getObject();

        Assert.assertEquals(proxiedHello.sayHello("jy"), "HELLO JY");
        Assert.assertEquals(proxiedHello.sayHi("jy"), "HI JY");
        Assert.assertEquals(proxiedHello.sayThankYou("jy"), "Thank You jy");

    }

    static class UppercaseAdvice implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation methodInvocation) throws Throwable {
            //MethodInvocation은 메소드 정보와 함께 타깃 오브젝트를 알고있음.
            String ret = (String) methodInvocation.proceed();
            return ret.toUpperCase();
        }
    }

}
