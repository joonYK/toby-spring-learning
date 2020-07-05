package learningtest.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UppercaseHandler implements InvocationHandler {
    //어떤 종류의 타깃도 적용가능하도록 Object 사용.
    private Object target;

    public UppercaseHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Object ret = method.invoke(target, args);

        //메서드의 결과가 String 이면서 메소드의 이름이 say로 시작하는 경우만 대문자로 반환.
        if(ret instanceof String && method.getName().startsWith("say"))
            return ((String)ret).toUpperCase();
        else
            return ret;
    }
}
