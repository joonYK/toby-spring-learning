package user.service;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class TransactionAdvice implements MethodInterceptor {
    private PlatformTransactionManager transactionManager;

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    /**
     * 트랜잭션 부가기능 적용.
     * @param methodInvocation 타깃을 호출하는 기능을 가진 콜백 오브젝트를 프록시로부터 받음. 특정 타깃에 의존 X
     */
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            /*
             *  콜백을 호출해서 타깃의 메서드를 실행. 타깃 메서드 호출 전후로 필요한 부가기느을 넣을 수 있다.
             *  경우에 따라 타깃이 아예 호출되지 않게 하거나 재시도를 위한 반복적인 호출도 가능.
             */
            Object ret = methodInvocation.proceed();
            this.transactionManager.commit(status);
            return ret;
        } catch (RuntimeException e) {
            this.transactionManager.rollback(status);
            throw e;
        }
    }
}
