package springbook.learningtest.ioc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.support.StaticApplicationContext;
import springbook.learningtest.ioc.bean.Hello;
import springbook.learningtest.ioc.bean.StringPrinter;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationContextTest {

    @Test
    void registerBean() {
        StaticApplicationContext ac = new StaticApplicationContext();
        ac.registerSingleton("hello1", Hello.class);

        RootBeanDefinition helloDef = new RootBeanDefinition(Hello.class);
        helloDef.getPropertyValues().addPropertyValue("name", "Spring");
        ac.registerBeanDefinition("hello2", helloDef);

        Hello hello1 = ac.getBean("hello1", Hello.class);
        assertThat(hello1).isNotNull();

        Hello hello2 = ac.getBean("hello2", Hello.class);
        assertThat(hello2.sayHello()).isEqualTo("Hello Spring");

        assertThat(hello1).isNotSameAs(hello2);
        assertThat(ac.getBeanFactory().getBeanDefinitionCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("두 개의 빈을 등록하고 DI 한 후 두 개의 빈이 서로 연동해서 동작하는지 확인")
    void registerBeanWithDependency() {
        StaticApplicationContext ac = new StaticApplicationContext();

        // SpringPrinter 클래스 타입의 printer 라는 이름을 가진 빈을 등록한다.
        ac.registerBeanDefinition("printer", new RootBeanDefinition(StringPrinter.class));

        BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
        helloDef.getPropertyValues().addPropertyValue("name", "Spring");
        helloDef.getPropertyValues().addPropertyValue("printer",
                new RuntimeBeanReference("printer"));

        ac.registerBeanDefinition("hello", helloDef);

        Hello hello = ac.getBean("hello", Hello.class);
        hello.print();

        assertThat(ac.getBean("printer").toString()).isEqualTo("Hello Spring");
    }
}