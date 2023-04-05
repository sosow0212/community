package yoon.community.domain;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static yoon.community.factory.CreateMessageFactory.createMessage;
import static yoon.community.factory.UserFactory.createUser;
import static yoon.community.factory.UserFactory.createUser2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import yoon.community.domain.member.Member;
import yoon.community.domain.message.Message;

public class MessageTest {

    private final Member sender = createUser();
    private final Member receiver = createUser2();

    @Test
    @DisplayName("전송자가 메시지를 삭제한다.")
    void delete_message_by_sender_success() {
        // given
        Message message = createMessage(sender, receiver);

        // when
        message.deleteBySender();

        // then
        assertThat(message.isDeletedBySender()).isEqualTo(true);
    }

    @Test
    @DisplayName("수신자가 메시지를 삭제한다.")
    void delete_message_by_receiver_success() {
        // given
        Message message = createMessage(sender, receiver);

        // when
        message.deleteByReceiver();

        // then
        assertThat(message.isDeletedByReceiver()).isEqualTo(true);
    }

    @Test
    @DisplayName("메시지를 삭제한다.")
    void delete_message_success() {
        // given
        Message message = createMessage(sender, receiver);

        // when
        message.deleteByReceiver();
        message.deleteBySender();

        // then
        assertThat(message.isDeletedMessage()).isEqualTo(true);
    }
}
