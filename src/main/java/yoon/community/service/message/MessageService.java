package yoon.community.service.message;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoon.community.dto.message.MessageCreateRequest;
import yoon.community.dto.message.MessageDto;
import yoon.community.entity.message.Message;
import yoon.community.entity.user.User;
import yoon.community.exception.MemberNotEqualsException;
import yoon.community.exception.MemberNotFoundException;
import yoon.community.exception.MessageNotFoundException;
import yoon.community.repository.message.MessageRepository;
import yoon.community.repository.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Transactional
    public MessageDto createMessage(User sender, MessageCreateRequest req) {
        User receiver = userRepository.findByNickname(req.getReceiverNickname())
                .orElseThrow(MemberNotFoundException::new);
        Message message = new Message(req.getTitle(), req.getContent(), sender, receiver);
        return MessageDto.toDto(messageRepository.save(message));
    }

    @Transactional(readOnly = true)
    public List<MessageDto> receiveMessages(User user) {
        List<Message> messageList = messageRepository.findAllByReceiverAndDeletedByReceiverFalseOrderByIdDesc(user);
        List<MessageDto> messageDtoList = messageList.stream()
                .map(message -> MessageDto.toDto(message))
                .collect(Collectors.toList());
        return messageDtoList;
    }


    @Transactional(readOnly = true)
    public MessageDto receiveMessage(int id, User user) {
        Message message = messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);
        validateReceiveMessage(user, message);
        return MessageDto.toDto(message);
    }

    private void validateReceiveMessage(User user, Message message) {
        if (message.getReceiver() != user) {
            throw new MemberNotEqualsException();
        }
        if (message.isDeletedByReceiver()) {
            throw new MessageNotFoundException();
        }
    }

    @Transactional(readOnly = true)
    public List<MessageDto> sendMessages(User user) {
        List<Message> messageList = messageRepository.findAllBySenderAndDeletedBySenderFalseOrderByIdDesc(user);
        List<MessageDto> messageDtoList = messageList.stream()
                .map(message -> MessageDto.toDto(message))
                .collect(Collectors.toList());
        return messageDtoList;
    }

    @Transactional(readOnly = true)
    public MessageDto sendMessage(int id, User user) {
        Message message = messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);
        validateSendMessage(user, message);
        return MessageDto.toDto(message);
    }

    private void validateSendMessage(User user, Message message) {
        if (message.getSender() != user) {
            throw new MemberNotEqualsException();
        }
        if (message.isDeletedByReceiver()) {
            throw new MessageNotFoundException();
        }
    }

    @Transactional
    public void deleteMessageByReceiver(int id, User user) {
        Message message = messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);
        processDeleteReceiverMessage(user, message);
        checkIsMessageDeletedBySenderAndReceiver(message);
    }

    private void processDeleteReceiverMessage(User user, Message message) {
        if (message.getReceiver().equals(user)) {
            message.deleteByReceiver();
            return;
        }
        throw new MemberNotEqualsException();
    }

    private void checkIsMessageDeletedBySenderAndReceiver(Message message) {
        if (message.isDeletedMessage()) {
            messageRepository.delete(message);
        }
    }

    @Transactional
    public void deleteMessageBySender(int id, User user) {
        Message message = messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);
        processDeleteSenderMessage(user, message);
        checkIsMessageDeletedBySenderAndReceiver(message);
    }

    private void processDeleteSenderMessage(User user, Message message) {
        if (message.getSender().equals(user)) {
            message.deleteBySender();
            return;
        }
        throw new MemberNotEqualsException();
    }
}
