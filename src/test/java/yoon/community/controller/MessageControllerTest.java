package yoon.community.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static yoon.community.factory.UserFactory.createUserWithAdminRole;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.Optional;
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
import yoon.community.domain.member.Member;
import yoon.community.dto.message.MessageCreateRequest;
import yoon.community.repository.member.MemberRepository;
import yoon.community.service.message.MessageService;

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
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(messageController).build();
    }

    @Test
    @DisplayName("메시지를 생성한다.")
    void create_message_success() throws Exception {
        // given
        MessageCreateRequest req = new MessageCreateRequest("타이틀", "내용", "유저닉네임");

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
    @DisplayName("받은 쪽지들을 조회한다.")
    void receive_messages_success() throws Exception {
        // given
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
    @DisplayName("받은 쪽지를 개별 조회한다.")
    void receiver_message_success() throws Exception {
        // given
        Long id = 1L;

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
    @DisplayName("보낸 쪽지들을 확인한다.")
    void send_messages_success() throws Exception {
        // given
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
    @DisplayName("보낸 쪽지를 개별 확인한다.")
    void send_message_success() throws Exception {
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
    @DisplayName("받은 쪽지를 삭제한다.")
    void delete_received_message_success() throws Exception {
        // given
        Long id = 1L;

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
    @DisplayName("보낸 쪽지를 삭제한다.")
    void delete_sender_message_success() throws Exception {
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
