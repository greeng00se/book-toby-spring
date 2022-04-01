package springbook.learningtest.factorybean;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Message {

    private final String text;

    public static Message newMessage(String text) {
        return new Message(text);
    }
}
