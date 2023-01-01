package yoon.community.factory;

import yoon.community.entity.message.Message;
import yoon.community.entity.member.Member;

import static yoon.community.factory.UserFactory.createUser;
import static yoon.community.factory.UserFactory.createUser2;

public class CreateMessageFactory {
    public static Message createMessage() {
        return new Message("title", "content", createUser(), createUser2());
    }

    public static Message createMessage(Member sender, Member receiver) {
        return new Message("title", "content", sender, receiver);
    }
}
