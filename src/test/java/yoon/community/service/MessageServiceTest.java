package yoon.community.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static yoon.community.factory.CreateMessageFactory.createMessage;
import static yoon.community.factory.UserFactory.createUser;
import static yoon.community.factory.UserFactory.createUser2;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yoon.community.domain.member.Member;
import yoon.community.domain.message.Message;
import yoon.community.dto.message.MessageDto;
import yoon.community.repository.member.MemberRepository;
import yoon.community.repository.message.MessageRepository;
import yoon.community.service.message.MessageService;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    @InjectMocks
    MessageService messageService;

    @Mock
    MessageRepository messageRepository;

    @Mock
    MemberRepository memberRepository;

//    @Test
//    @DisplayName("createMessage 서비스 테스트")
//    void createMessageTest() {
//        // given
//        User receiver = createUser();
//        User sender = createUser2();
//        MessageCreateRequest req = new MessageCreateRequest("title", "content", receiver.getNickname());
//        Message message = new Message(req.getTitle(), req.getContent(), sender, receiver);
//        given(userRepository.findByNickname(anyString())).willReturn(Optional.of(receiver));
//        given(messageRepository.save(message)).willReturn(message);
//
//        // when
//        MessageDto result = messageService.createMessage(sender, req);
//
//        // then
//        assertThat(result.getTitle()).isEqualTo("title");
//    }

    @Test
    @DisplayName("receiveMessage 서비스 테스트")
    void receiveMessageTest() {
        // given
        Member member = createUser();
        Message message = createMessage();
        message.setReceiver(member);
        given(messageRepository.findById(anyLong())).willReturn(Optional.of(message));

        // when
        MessageDto result = messageService.receiveMessage(anyLong(), member);

        // then
        assertThat(result.getTitle()).isEqualTo("title");
    }

    @Test
    @DisplayName("sendMessages 서비스 테스트")
    void sendMessagesTest() {
        // given
        List<Message> list = new ArrayList<>();
        list.add(createMessage());
        list.add(createMessage());
        given(messageRepository.findAllBySenderAndDeletedBySenderFalseOrderByIdDesc(any())).willReturn(list);

        // when
        List<MessageDto> result = messageService.sendMessages(createUser());

        // then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("sendMessage 서비스 테스트")
    void sendMessageTest() {
        // given
        Member member = createUser();
        Message message = createMessage();
        message.setSender(member);
        given(messageRepository.findById(anyLong())).willReturn(Optional.of(message));

        // when
        MessageDto result = messageService.sendMessage(anyLong(), member);

        // then
        assertThat(result.getTitle()).isEqualTo("title");
    }

    @Test
    @DisplayName("deleteMessageByReceiverNotDeletableTest 서비스 테스트")
    void deleteMessageByReceiverNotDeletableTest() {
        // given
        Message message = createMessage();
        Member member = createUser();
        message.setReceiver(member);
        given(messageRepository.findById(anyLong())).willReturn(Optional.of(message));

        // when
        messageService.deleteMessageByReceiver(anyLong(), member);

        // then
        verify(messageRepository, never()).delete(any(Message.class));
    }

    @Test
    @DisplayName("deleteMessageBySenderNotDeletableTest 서비스 테스트")
    void deleteMessageBySenderNotDeletableTest() {
        // given
        Message message = createMessage();
        Member member = createUser();
        message.setSender(member);
        given(messageRepository.findById(anyLong())).willReturn(Optional.of(message));

        // when
        messageService.deleteMessageBySender(anyLong(), member);

        // then
        verify(messageRepository, never()).delete(any(Message.class));
    }

    @Test
    @DisplayName("deleteMessage 서비스 테스트")
    void deleteMessageTest() {
        // given
        Member receiver = createUser();
        Member sender = createUser2();
        Message message = createMessage();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setDeletedBySender(true);
        given(messageRepository.findById(anyLong())).willReturn(Optional.of(message));

        // when
        messageService.deleteMessageByReceiver(anyLong(), receiver);

        // then
        verify(messageRepository).delete(any(Message.class));
    }
}
