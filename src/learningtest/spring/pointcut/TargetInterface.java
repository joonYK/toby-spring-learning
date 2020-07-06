package learningtest.spring.pointcut;

public interface TargetInterface {
    void Hello();
    void hello(String a);
    int minus(int a, int b) throws RuntimeException;
    int plus(int a, int b);
}
