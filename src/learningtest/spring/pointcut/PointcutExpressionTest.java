package learningtest.spring.pointcut;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

public class PointcutExpressionTest {

    @Test
    public void methodSignaturePointcut() throws SecurityException, NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();

        //Target 클래스 minus() 메서드 시그니처
        pointcut.setExpression("execution(public int learningtest.spring.pointcut.Target.minus(int, int) throws java.lang.RuntimeException)");

        //Target.minus(). 클래스 필터와 메소드 매처를 가져와 각각 비교.
        Assert.assertEquals(pointcut.getClassFilter().matches(Target.class) &&
                pointcut.getMethodMatcher().matches(Target.class.getMethod("minus", int.class, int.class), null), true);

        //Target.plus(). 메서드 매처에서 실패
        Assert.assertEquals(pointcut.getClassFilter().matches(Target.class) &&
                pointcut.getMethodMatcher().matches(Target.class.getMethod("plus", int.class, int.class), null), false);

        //Bean.method(). 클래스 필터에서 실패
        Assert.assertEquals(pointcut.getClassFilter().matches(Bean.class) &&
                pointcut.getMethodMatcher().matches(Target.class.getMethod("method"), null), false);
    }
}
