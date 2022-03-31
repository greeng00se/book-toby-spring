package springbook.user.service.hanlder;

import lombok.Setter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Setter
public class TransactionHandler implements InvocationHandler {

    // 부가 기능을 제공할 타깃 오브젝트
    private Object target;
    // 트랜잭션 기능을 제공하는 데 필요한 트랜잭션 매니저
    private PlatformTransactionManager transactionManager;
    // 트랜잭션을 적용할 메소드 이름 패턴
    private String pattern;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().startsWith(pattern)) {
            return invokeInTransaction(method, args);
        } else {
            return method.invoke(target, args);
        }
    }

    private Object invokeInTransaction(Method method, Object[] args) throws Throwable {
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            // 트랜잭션 시작하고 타켓 메서드 호출, 예외 발생하지 않으면 커밋
            Object ret = method.invoke(target, args);
            this.transactionManager.commit(status);
            return ret;
        } catch (InvocationTargetException e) {
            // 예외가 발생하면 트랜잭션 롤백
            this.transactionManager.rollback(status);
            throw e.getTargetException();
        }
    }
}
