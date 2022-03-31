package springbook.learningtest.proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
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
        pfBean.setTarget(new HelloTarget());
        pfBean.addAdvice(new UppercaseAdvice());

        Hello proxiedHello = (Hello) pfBean.getObject();
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
}
