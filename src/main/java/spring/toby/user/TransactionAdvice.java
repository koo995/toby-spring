package spring.toby.user;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

// MethodInterceptor 은 advice 을 상속받는다.
public class TransactionAdvice implements MethodInterceptor {
    private final PlatformTransactionManager transactionManager;

    public TransactionAdvice(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    /**
     * 타깃을 호출하는 기능을 가진 콜백 오브젝트를 프록시로부터 받는다.
     * 덕분에 어드바이스는 특정 타깃에 의존하지 않고 재사용 가능하다.
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            // 콜백을 호출해서 타깃의 메소드를 실행한다.
            // 타깃 메서드 호출 전후로 필요한 부가기능을 넣을 수 있다.
            // 경우에 따라서 타깃을 호출하지 않거나 여러번 호출 가능
            Object ret = invocation.proceed();
            this.transactionManager.commit(status);
            return ret;
        } catch (RuntimeException e) {
            this.transactionManager.rollback(status);
            throw e;
        }
    }
}
