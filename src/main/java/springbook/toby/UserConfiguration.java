package springbook.toby;

import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import springbook.learningtest.jdk.NameMatchClassMethodPointcut;
import springbook.user.service.TransactionAdvice;

@Configuration
public class UserConfiguration {

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        return new DefaultAdvisorAutoProxyCreator();
    }
    
    @Bean
    public DefaultPointcutAdvisor transactionAdvisor() {
        DefaultPointcutAdvisor defaultPointcutAdvisor = new DefaultPointcutAdvisor();
        defaultPointcutAdvisor.setPointcut(transactionPointcut());
        defaultPointcutAdvisor.setAdvice(transactionAdvice());
        return defaultPointcutAdvisor;
    }

    @Bean
    public NameMatchClassMethodPointcut transactionPointcut() {
        NameMatchClassMethodPointcut nameMatchClassMethodPointcut = new NameMatchClassMethodPointcut();
        nameMatchClassMethodPointcut.setMappedClassName("*Service");
        nameMatchClassMethodPointcut.setMappedName("upgrade*");
        return nameMatchClassMethodPointcut;
    }

    @Autowired PlatformTransactionManager transactionManager;

    @Bean
    public TransactionAdvice transactionAdvice() {
        TransactionAdvice transactionAdvice = new TransactionAdvice();
        transactionAdvice.setTransactionManager(transactionManager);
        return transactionAdvice;
    }
}