package springbook.learningtest.proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.assertThat;

public class DynamicProxyTest {

    @Test
    void simpleProxy() {
        Hello dynamicProxyHello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] { Hello.class },
                new UppercaseHandler(new HelloTarget())
        );

        assertThat(dynamicProxyHello.sayHello("GOOSE")).isEqualTo("HELLO GOOSE");
        assertThat(dynamicProxyHello.sayHi("GOOSE")).isEqualTo("HI GOOSE");
        assertThat(dynamicProxyHello.sayThankYou("GOOSE")).isEqualTo("THANK YOU GOOSE");
    }

    @Test
    void proxyFactoryBean() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget()); // 타깃 설정
        pfBean.addAdvice(new UppercaseAdvice()); // 부가기능을 담은 어드바이스 추가 여러개 추가 가능
        Hello proxiedHello = (Hello) pfBean.getObject(); // 생성된 프록시를 가져온다.

        assertThat(proxiedHello.sayHello("GOOSE")).isEqualTo("HELLO GOOSE");
        assertThat(proxiedHello.sayHi("GOOSE")).isEqualTo("HI GOOSE");
        assertThat(proxiedHello.sayThankYou("GOOSE")).isEqualTo("THANK YOU GOOSE");
    }

    private class UppercaseAdvice implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String ret = (String) invocation.proceed();
            return ret.toUpperCase();
        }
    }

    @Test
    void pointcutAdvisor() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");

        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

        Hello proxiedHello = (Hello) pfBean.getObject();

        assertThat(proxiedHello.sayHello("GOOSE")).isEqualTo("HELLO GOOSE");
        assertThat(proxiedHello.sayHi("GOOSE")).isEqualTo("HI GOOSE");
        assertThat(proxiedHello.sayThankYou("GOOSE")).isEqualTo("Thank You GOOSE");
    }

    @Test
    void classNamePointcutAdvisor() {
        NameMatchMethodPointcut classMethodPointcut = new NameMatchMethodPointcut() {
            public ClassFilter getClassFilter() {
                return new ClassFilter() {
                    @Override
                    public boolean matches(Class<?> clazz) {
                        return clazz.getSimpleName().startsWith("HelloT");
                    }
                };
            }
        };

        classMethodPointcut.setMappedName("sayH*");

        checkAdviced(new HelloTarget(), classMethodPointcut, true);

        class HelloWorld extends HelloTarget {};
        checkAdviced(new HelloWorld(), classMethodPointcut, false);
    }

    private void checkAdviced(Object target, Pointcut pointcut, boolean adviced) {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(target);
        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
        Hello proxiedHello = (Hello) pfBean.getObject();

        if (adviced) {
            assertThat(proxiedHello.sayHello("Cookie")).isEqualTo("HELLO COOKIE");
            assertThat(proxiedHello.sayHi("Cookie")).isEqualTo("HI COOKIE");
            assertThat(proxiedHello.sayThankYou("Cookie")).isEqualTo("Thank You Cookie");
        } else {
            assertThat(proxiedHello.sayHello("Cookie")).isEqualTo("Hello Cookie");
            assertThat(proxiedHello.sayHi("Cookie")).isEqualTo("Hi Cookie");
            assertThat(proxiedHello.sayThankYou("Cookie")).isEqualTo("Thank You Cookie");
        }
    }
}
