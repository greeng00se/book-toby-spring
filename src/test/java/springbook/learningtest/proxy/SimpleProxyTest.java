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
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] { Hello.class },
                new UppercaseHandler(new HelloTarget())
        );

        assertThat(proxiedHello.sayHello("GOOSE")).isEqualTo("HELLO GOOSE");
        assertThat(proxiedHello.sayHi("GOOSE")).isEqualTo("HI GOOSE");
        assertThat(proxiedHello.sayThankYou("GOOSE")).isEqualTo("THANK YOU GOOSE");
    }
}
