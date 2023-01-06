package yoon.community.service.message;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoon.community.dto.message.MessageCreateRequest;
import yoon.community.dto.message.MessageDto;
import yoon.community.domain.message.Message;
import yoon.community.domain.member.Member;
import yoon.community.exception.MemberNotEqualsException;
import yoon.community.exception.MemberNotFoundException;
import yoon.community.exception.MessageNotFoundException;
import yoon.community.repository.message.MessageRepository;
import yoon.community.repository.member.MemberRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public MessageDto createMessage(Member sender, MessageCreateRequest req) {
        Member receiver = memberRepository.findByNickname(req.getReceiverNickname())
                .orElseThrow(MemberNotFoundException::new);
        Message message = new Message(req.getTitle(), req.getContent(), sender, receiver);
        return MessageDto.toDto(messageRepository.save(message));
    }

    @Transactional(readOnly = true)
    public List<MessageDto> receiveMessages(Member member) {
        List<Message> messageList = messageRepository.findAllByReceiverAndDeletedByReceiverFalseOrderByIdDesc(member);
        List<MessageDto> messageDtoList = messageList.stream()
                .map(message -> MessageDto.toDto(message))
                .collect(Collectors.toList());
        return messageDtoList;
    }


    @Transactional(readOnly = true)
    public MessageDto receiveMessage(Long id, Member member) {
        Message message = messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);
        validateReceiveMessage(member, message);
        return MessageDto.toDto(message);
    }

    private void validateReceiveMessage(Member member, Message message) {
        if (message.getReceiver() != member) {
            throw new MemberNotEqualsException();
        }
        if (message.isDeletedByReceiver()) {
            throw new MessageNotFoundException();
        }
    }

    @Transactional(readOnly = true)
    public List<MessageDto> sendMessages(Member member) {
        List<Message> messageList = messageRepository.findAllBySenderAndDeletedBySenderFalseOrderByIdDesc(member);
        List<MessageDto> messageDtoList = messageList.stream()
                .map(message -> MessageDto.toDto(message))
                .collect(Collectors.toList());
        return messageDtoList;
    }

    @Transactional(readOnly = true)
    public MessageDto sendMessage(Long id, Member member) {
        Message message = messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);
        validateSendMessage(member, message);
        return MessageDto.toDto(message);
    }

    private void validateSendMessage(Member member, Message message) {
        if (message.getSender() != member) {
            throw new MemberNotEqualsException();
        }
        if (message.isDeletedByReceiver()) {
            throw new MessageNotFoundException();
        }
    }

    @Transactional
    public void deleteMessageByReceiver(Long id, Member member) {
        Message message = messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);
        processDeleteReceiverMessage(member, message);
        checkIsMessageDeletedBySenderAndReceiver(message);
    }

    private void processDeleteReceiverMessage(Member member, Message message) {
        if (message.getReceiver().equals(member)) {
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
    public void deleteMessageBySender(Long id, Member member) {
        Message message = messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);
        processDeleteSenderMessage(member, message);
        checkIsMessageDeletedBySenderAndReceiver(message);
    }

    private void processDeleteSenderMessage(Member member, Message message) {
        if (message.getSender().equals(member)) {
            message.deleteBySender();
            return;
        }
        throw new MemberNotEqualsException();
    }
}
