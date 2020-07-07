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

    @Test
    public void pointcut() throws Exception {
        targetClassPointcutMatches("execution(* *(..))", true, true, true, true, true, true);
        targetClassPointcutMatches("execution(* hello())", true, false, false, false, false, false);
        targetClassPointcutMatches("execution(* hello(String))", false, true, false, false, false, false);
        targetClassPointcutMatches("execution(* meth*(..))", false, false, false, false, true, true);
        targetClassPointcutMatches("execution(* *(int, int))", false, false, true, true, false, false);
        targetClassPointcutMatches("execution(* *())", true, false, false, false, true, true);
        targetClassPointcutMatches("execution(* learningtest.spring.pointcut.Target.*(..))", true, true, true, true, true, false);
        targetClassPointcutMatches("execution(* learningtest.spring.pointcut.*.*(..))", true, true, true, true, true, true);
        targetClassPointcutMatches("execution(* learningtest.spring.pointcut..*.*(..))", true, true, true, true, true, true);
        targetClassPointcutMatches("execution(* learningtest..*.*(..))", true, true, true, true, true, true);
        targetClassPointcutMatches("execution(* com..*.*(..))", false, false, false, false, false, false);
        targetClassPointcutMatches("execution(* *..Target.*(..))", true, true, true, true, true, false);
        targetClassPointcutMatches("execution(* *..Tar*.*(..))", true, true, true, true, true, false);
        targetClassPointcutMatches("execution(* *..*get.*(..))", true, true, true, true, true, false);
        targetClassPointcutMatches("execution(* *..B*.*(..))", false, false, false, false, false, true);
        targetClassPointcutMatches("execution(* *..TargetInterface.*(..))", true, true, true, true, false, false);
        targetClassPointcutMatches("execution(* *(..) throws Runtime*)", false, false, false, true, false, true);
        targetClassPointcutMatches("execution(int *(..))", false, false, true, true, false, false);
        targetClassPointcutMatches("execution(void *(..))", true, true, false, false, true, true);

    }

    private void targetClassPointcutMatches(String expression, boolean... expected) throws Exception {
        pointcutMatches(expression, expected[0], Target.class, "hello");
        pointcutMatches(expression, expected[1], Target.class, "hello", String.class);
        pointcutMatches(expression, expected[2], Target.class, "plus", int.class, int.class);
        pointcutMatches(expression, expected[3], Target.class, "minus", int.class, int.class);
        pointcutMatches(expression, expected[4], Target.class, "method");
        pointcutMatches(expression, expected[5], Bean.class, "method");
    }

    private void pointcutMatches(String expression, Boolean expected, Class<?> clazz, String methodName, Class<?>... args) throws Exception {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(expression);

        Assert.assertEquals(pointcut.getClassFilter().matches(clazz)
                && pointcut.getMethodMatcher().matches(clazz.getMethod(methodName, args), null), expected);

    }

}
