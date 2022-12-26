package yoon.community.domain;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static yoon.community.factory.CreateMessageFactory.createMessage;
import static yoon.community.factory.UserFactory.createUser;
import static yoon.community.factory.UserFactory.createUser2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import yoon.community.entity.message.Message;
import yoon.community.entity.user.User;

public class MessageTest {
    User sender = createUser();
    User receiver = createUser2();

    @Test
    @DisplayName("deleteBySender 테스트")
    public void deleteBySenderTest() {
        // given
        Message message = createMessage(sender, receiver);

        // when
        message.deleteBySender();

        // then
        assertThat(message.isDeletedBySender()).isEqualTo(true);
    }

    @Test
    @DisplayName("deleteByReceiver 테스트")
    public void deleteByReceiverTest() {
        // given
        Message message = createMessage(sender, receiver);

        // when
        message.deleteByReceiver();

        // then
        assertThat(message.isDeletedByReceiver()).isEqualTo(true);
    }

    @Test
    @DisplayName("메시지 삭제 테스트")
    public void deleteMessageTest() {
        // given
        Message message = createMessage(sender, receiver);

        // when
        message.deleteByReceiver();
        message.deleteBySender();

        // then
        assertThat(message.isDeletedMessage()).isEqualTo(true);
    }
}
