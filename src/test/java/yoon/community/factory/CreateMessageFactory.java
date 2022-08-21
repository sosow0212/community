package yoon.community.factory;

import yoon.community.entity.message.Message;

import static yoon.community.factory.UserFactory.createUser;
import static yoon.community.factory.UserFactory.createUser2;

public class CreateMessageFactory {
    public static Message createMessage() {
        return new Message("title", "content", createUser(), createUser2());
    }
}
