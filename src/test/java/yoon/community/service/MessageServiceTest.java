package yoon.community.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
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
import yoon.community.domain.member.Authority;
import yoon.community.domain.member.Member;
import yoon.community.domain.message.Message;
import yoon.community.dto.message.MessageCreateRequest;
import yoon.community.dto.message.MessageDto;
import yoon.community.exception.MemberNotEqualsException;
import yoon.community.exception.MessageNotFoundException;
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

    @Test
    @DisplayName("메시지를 생성한다.")
    void create_message_success() {
        // given
        Member sender = createUser();
        Member receiver = new Member("receiver", "1234", Authority.ROLE_USER);
        MessageCreateRequest req = new MessageCreateRequest("제목", "내용", receiver.getNickname());
        Message message = new Message(req.getTitle(), req.getContent(), sender, receiver);

        given(memberRepository.findByNickname(req.getReceiverNickname())).willReturn(Optional.of(receiver));
        given(messageRepository.save(argThat(arg -> arg.getTitle().equals("제목")))).willReturn(message);

        // when
        MessageDto result = messageService.createMessage(sender, req);

        // then
        assertThat(result.getTitle()).isEqualTo(req.getTitle());
        assertThat(result.getReceiverName()).isEqualTo(receiver.getNickname());
    }

    @Test
    @DisplayName("받은 메시지 리스트를 조회한다.")
    void receive_messages_success() {
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
    @DisplayName("받은 메시지 한 개를 조회한다.")
    void receive_message_success() {
        // given
        Long id = 1L;
        Member receiver = createUser();
        Message message = createMessage(createUser(), receiver);

        given(messageRepository.findById(id)).willReturn(Optional.of(message));

        // when
        MessageDto result = messageService.receiveMessage(id, receiver);

        // then
        assertThat(result.getReceiverName()).isEqualTo(receiver.getNickname());
    }

    @Test
    @DisplayName("메시지의 수신자와, 요청을 보낸 유저가 동일하지 않은 경우 예외를 발생시킨다.")
    void throws_exception_when_receiver_invalid() {
        // given
        Long id = 1L;
        Member receiver = new Member(id, "username", "1", "name", "nickname", Authority.ROLE_USER, false);
        Message message = new Message("title", "content", createUser(), createUser2());

        given(messageRepository.findById(id)).willReturn(Optional.of(message));

        // when & then
        assertThatThrownBy(() -> messageService.receiveMessage(id, receiver))
                .isInstanceOf(MemberNotEqualsException.class);
    }

    @Test
    @DisplayName("메시지를 수신자가 이미 삭제해서 없는 경우 예외를 발생시킨다.")
    void throws_exception_when_receiver_already_deleted_message() {
        // given
        Long id = 1L;
        Member receiver = new Member(id, "username", "1", "name", "nickname", Authority.ROLE_USER, false);
        Message message = new Message("title", "content", createUser(), receiver);
        message.deleteByReceiver();

        given(messageRepository.findById(id)).willReturn(Optional.of(message));

        // when & then
        assertThatThrownBy(() -> messageService.receiveMessage(id, receiver))
                .isInstanceOf(MessageNotFoundException.class);
    }

    @Test
    @DisplayName("메시지 여러 개를 전송한다.")
    void send_messages_success() {
        // given
        List<Message> list = new ArrayList<>();
        list.add(createMessage());
        list.add(createMessage());
        given(messageRepository.findAllBySenderAndDeletedBySenderFalseOrderByIdDesc(any())).willReturn(list);

        // when
        List<MessageDto> result = messageService.sendMessages(createUser());

        // then
        assertThat(result.size()).isEqualTo(list.size());
    }

    @Test
    @DisplayName("메시지 한 개를 전송한다.")
    void send_message_success() {
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
    @DisplayName("수신자가 메시지를 삭제했지만, 송신자가 삭제하지 않아서 DB에서는 삭제 되지 않는다.")
    void delete_message_by_receiver_not_deletable_on_db() {
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
    @DisplayName("송신자 메시지를 삭제했지만, 수신자가 삭제하지 않아서 DB에서는 삭제 되지 않는다.")
    void delete_message_by_sender_not_deletable_on_db() {
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
    @DisplayName("메시지를 삭제한다.")
    void delete_message_success() {
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
