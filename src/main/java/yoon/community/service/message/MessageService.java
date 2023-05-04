package yoon.community.service.message;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoon.community.domain.member.Member;
import yoon.community.domain.message.Message;
import yoon.community.dto.message.MessageCreateRequest;
import yoon.community.dto.message.MessageDto;
import yoon.community.exception.MemberNotEqualsException;
import yoon.community.exception.MemberNotFoundException;
import yoon.community.exception.MessageNotFoundException;
import yoon.community.repository.member.MemberRepository;
import yoon.community.repository.message.MessageRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;

    public MessageService(final MessageRepository messageRepository, final MemberRepository memberRepository) {
        this.messageRepository = messageRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public MessageDto createMessage(final Member sender, final MessageCreateRequest req) {
        Member receiver = getReceiver(req);
        Message message = getMessage(sender, req, receiver);

        return MessageDto.toDto(messageRepository.save(message));
    }

    private Member getReceiver(final MessageCreateRequest req) {
        return memberRepository.findByNickname(req.getReceiverNickname())
                .orElseThrow(MemberNotFoundException::new);
    }

    private Message getMessage(final Member sender, final MessageCreateRequest req, final Member receiver) {
        return new Message(req.getTitle(), req.getContent(), sender, receiver);
    }

    @Transactional(readOnly = true)
    public List<MessageDto> receiveMessages(final Member member) {
        return messageRepository.findAllByReceiverAndDeletedByReceiverFalseOrderByIdDesc(member).stream()
                .map(MessageDto::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MessageDto receiveMessage(final Long id, final Member receiver) {
        Message message = messageRepository.findById(id)
                .orElseThrow(MessageNotFoundException::new);

        validateReceiveMessage(receiver, message);
        return MessageDto.toDto(message);
    }

    private void validateReceiveMessage(final Member member, final Message message) {
        if (!message.getReceiver().isSameMemberId(member.getId())) {
            throw new MemberNotEqualsException();
        }

        if (message.isDeletedByReceiver()) {
            throw new MessageNotFoundException();
        }
    }

    @Transactional(readOnly = true)
    public List<MessageDto> sendMessages(final Member member) {
        return messageRepository.findAllBySenderAndDeletedBySenderFalseOrderByIdDesc(member).stream()
                .map(MessageDto::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MessageDto sendMessage(final Long id, final Member member) {
        Message message = messageRepository.findById(id)
                .orElseThrow(MessageNotFoundException::new);

        validateSendMessage(member, message);
        return MessageDto.toDto(message);
    }

    private void validateSendMessage(final Member member, final Message message) {
        if (!message.isSender(member)) {
            throw new MemberNotEqualsException();
        }

        if (message.isDeletedByReceiver()) {
            throw new MessageNotFoundException();
        }
    }

    @Transactional
    public void deleteMessageByReceiver(final Long id, final Member member) {
        Message message = messageRepository.findById(id)
                .orElseThrow(MessageNotFoundException::new);

        processDeleteReceiverMessage(member, message);
        checkIsMessageDeletedBySenderAndReceiver(message);
    }

    private void processDeleteReceiverMessage(final Member member, final Message message) {
        if (message.getReceiver().isSameMemberId(member.getId())) {
            message.deleteByReceiver();
            return;
        }

        throw new MemberNotEqualsException();
    }

    private void checkIsMessageDeletedBySenderAndReceiver(final Message message) {
        if (message.isDeletedMessage()) {
            messageRepository.delete(message);
        }
    }

    @Transactional
    public void deleteMessageBySender(final Long id, final Member member) {
        Message message = messageRepository.findById(id)
                .orElseThrow(MessageNotFoundException::new);

        processDeleteSenderMessage(member, message);
        checkIsMessageDeletedBySenderAndReceiver(message);
    }

    private void processDeleteSenderMessage(final Member member, final Message message) {
        if (message.getSender().equals(member)) {
            message.deleteBySender();
            return;
        }

        throw new MemberNotEqualsException();
    }
}
