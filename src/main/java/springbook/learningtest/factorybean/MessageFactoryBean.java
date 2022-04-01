package springbook.learningtest.factorybean;

import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;

@Setter
public class MessageFactoryBean implements FactoryBean<Message> {
    String text;

    @Override
    public Message getObject() throws Exception {
        return Message.newMessage(this.text);
    }

    @Override
    public Class<? extends Message> getObjectType() {
        return Message.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
