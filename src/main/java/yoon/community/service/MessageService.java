package yoon.community.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
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
import yoon.community.repository.MessageRepository;
import yoon.community.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Transactional
    public MessageDto createMessage(MessageCreateRequest req) {
        User receiver = userRepository.findByNickname(req.getReceiverNickname()).orElseThrow(MemberNotFoundException::new);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User sender = userRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        Message message = new Message(req.getTitle(), req.getContent(), sender, receiver);
        messageRepository.save(message);

        return MessageDto.toDto(message);
    }

    @Transactional(readOnly = true)
    public List<MessageDto> receiveMessages() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);

        List<MessageDto> messageDtoList = new ArrayList<>();
        List<Message> messageList = messageRepository.findAllByReceiver(user);

        for(Message message : messageList) {
            if(!message.isDeletedByReceiver()) {
                messageDtoList.add(MessageDto.toDto(message));
            }
        }
        return messageDtoList;
    }


    @Transactional(readOnly = true)
    public MessageDto receiveMessage(int id) {
        Message message = messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);

        if(message.getReceiver() != user) {
            throw new MemberNotEqualsException();
        }

        if(message.isDeletedByReceiver()) {
            throw new MessageNotFoundException();
        }
        return MessageDto.toDto(message);
    }

    @Transactional(readOnly = true)
    public List<MessageDto> sendMessages() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);

        List<MessageDto> messageDtoList = new ArrayList<>();
        List<Message> messageList = messageRepository.findAllBySender(user);

        for(Message message : messageList) {
            if(!message.isDeletedBySender()) {
                messageDtoList.add(MessageDto.toDto(message));
            }
        }
        return messageDtoList;
    }

    @Transactional(readOnly = true)
    public MessageDto sendMessage(int id) {
        Message message = messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);

        if(message.getSender() != user) {
            throw new MemberNotEqualsException();
        }

        if(message.isDeletedByReceiver()) {
            throw new MessageNotFoundException();
        }
        return MessageDto.toDto(message);
    }

    @Transactional
    public void deleteMessageByReceiver(int id) {
        Message message = messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);

        if(message.getReceiver() == user) {
            message.deleteByReceiver();
        } else {
            throw new MemberNotEqualsException();
        }

        if(message.isDeletedMessage()) {
            // 수신, 송신자 둘다 삭제할 경우
            messageRepository.delete(message);
        }
    }

    @Transactional
    public void deleteMessageBySender(int id) {
        Message message = messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);

        if(message.getSender() == user) {
            message.deleteBySender();
        } else {
            throw new MemberNotEqualsException();
        }

        if(message.isDeletedMessage()) {
            messageRepository.delete(message);
        }
    }
}