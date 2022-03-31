package springbook.learningtest.proxy;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleProxyTest {

    @Test
    public void simpleProxy() {
        Hello hello = new HelloTarget();
        assertThat(hello.sayHello("GOOSE")).isEqualTo("Hello GOOSE");
        assertThat(hello.sayHi("GOOSE")).isEqualTo("Hi GOOSE");
        assertThat(hello.sayThankYou("GOOSE")).isEqualTo("Thank You GOOSE");
    }

    @Test
    public void ProxyUppercaseTest() {
        Hello hello = new HelloUppercase(new HelloTarget());
        assertThat(hello.sayHello("GOOSE")).isEqualTo("HELLO GOOSE");
        assertThat(hello.sayHi("GOOSE")).isEqualTo("HI GOOSE");
        assertThat(hello.sayThankYou("GOOSE")).isEqualTo("THANK YOU GOOSE");
    }

    @Test
    public void ProxyUppercaseHandlerTest() {
        Hello dynamicProxyHello = (Hello) Proxy.newProxyInstance(
                // 동적으로 생성되는 다이내믹 프록시 클래스 로딩에 사용할 클래스 로더
                getClass().getClassLoader(),
                // 구현할 인터페이스
                new Class[] { Hello.class },
                // 부가기능과 위임 코드를 담은 InvocationHandler
                new UppercaseHandler(new HelloTarget())
        );

        assertThat(dynamicProxyHello.sayHello("GOOSE")).isEqualTo("HELLO GOOSE");
        assertThat(dynamicProxyHello.sayHi("GOOSE")).isEqualTo("HI GOOSE");
        assertThat(dynamicProxyHello.sayThankYou("GOOSE")).isEqualTo("THANK YOU GOOSE");
    }
}
