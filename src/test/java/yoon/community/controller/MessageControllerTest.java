package yoon.community.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import yoon.community.controller.message.MessageController;
import yoon.community.dto.message.MessageCreateRequest;
import yoon.community.entity.member.Member;
import yoon.community.repository.member.MemberRepository;
import yoon.community.service.message.MessageService;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static yoon.community.factory.UserFactory.createUserWithAdminRole;

@ExtendWith(MockitoExtension.class)
public class MessageControllerTest {
    @InjectMocks
    MessageController messageController;

    @Mock
    MessageService messageService;
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    MemberRepository memberRepository;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(messageController).build();

    }


    @Test
    @DisplayName("쪽지 작성")
    public void createMessageTest() throws Exception {
        // given
        MessageCreateRequest req = new MessageCreateRequest("타이틀", "내용", "유저닉네임");

        // 테스트 코드 진행시 SecurityContext에 유저 정보 미리 담아두기
        Member member = createUserWithAdminRole();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "",
                Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        // when, then
        mockMvc.perform(
                        post("/api/messages")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))

                .andExpect(status().isCreated());

        verify(messageService).createMessage(member, req);
    }

    @Test
    @DisplayName("받은 쪽지함 확인")
    public void receiveMessagesTest() throws Exception {
        // given

        // 테스트 코드 진행시 SecurityContext에 유저 정보 미리 담아두기
        Member member = createUserWithAdminRole();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "",
                Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        // when, then
        mockMvc.perform(
                        get("/api/messages/receiver"))
                .andExpect(status().isOk());
        verify(messageService).receiveMessages(member);
    }

    @Test
    @DisplayName("받은 쪽지 개별 확인")
    public void receiveMessageTest() throws Exception {
        // given
        Long id = 1L;

        // 테스트 코드 진행시 SecurityContext에 유저 정보 미리 담아두기
        Member member = createUserWithAdminRole();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "",
                Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        // when, then
        mockMvc.perform(
                        get("/api/messages/receiver/{id}", id))
                .andExpect(status().isOk());
        verify(messageService).receiveMessage(id, member);
    }

    @Test
    @DisplayName("보낸 쪽지함 확인")
    public void sendMessagesTest() throws Exception {
        // given
        // 테스트 코드 진행시 SecurityContext에 유저 정보 미리 담아두기
        Member member = createUserWithAdminRole();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "",
                Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        // when, then
        mockMvc.perform(
                        get("/api/messages/sender"))
                .andExpect(status().isOk());
        verify(messageService).sendMessages(member);
    }

    @Test
    @DisplayName("보낸 쪽지 개별 확인")
    public void sendMessageTest() throws Exception {
        // given
        Long id = 1L;

        // 테스트 코드 진행시 SecurityContext에 유저 정보 미리 담아두기
        Member member = createUserWithAdminRole();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "",
                Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        // when, then
        mockMvc.perform(
                        get("/api/messages/sender/{id}", id))
                .andExpect(status().isOk());
        verify(messageService).sendMessage(id, member);
    }

    @Test
    @DisplayName("받은 쪽지 삭제")
    public void deleteReceiveMessageTest() throws Exception {
        // given
        Long id = 1L;

        // 테스트 코드 진행시 SecurityContext에 유저 정보 미리 담아두기
        Member member = createUserWithAdminRole();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "",
                Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        // when, then
        mockMvc.perform(
                        delete("/api/messages/receiver/{id}", id))
                .andExpect(status().isOk());
        verify(messageService).deleteMessageByReceiver(id, member);
    }

    @Test
    @DisplayName("보낸 쪽지 삭제")
    public void deleteSenderMessageTest() throws Exception {
        // given
        Long id = 1L;

        // 테스트 코드 진행시 SecurityContext에 유저 정보 미리 담아두기
        Member member = createUserWithAdminRole();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "",
                Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        // when, then
        mockMvc.perform(
                        delete("/api/messages/sender/{id}", id))
                .andExpect(status().isOk());
        verify(messageService).deleteMessageBySender(id, member);
    }

}
