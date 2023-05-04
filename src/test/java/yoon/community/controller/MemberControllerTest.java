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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import yoon.community.config.guard.LoginMemberArgumentResolver;
import yoon.community.controller.member.MemberController;
import yoon.community.domain.member.Member;
import yoon.community.dto.member.MemberEditRequestDto;
import yoon.community.repository.member.MemberRepository;
import yoon.community.service.member.MemberService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static yoon.community.factory.UserFactory.createUserWithAdminRole;

@ExtendWith(MockitoExtension.class)
public class MemberControllerTest {

    @InjectMocks
    MemberController memberController;

    @Mock
    MemberRepository memberRepository;

    @Mock
    MemberService memberService;

    @Mock
    LoginMemberArgumentResolver loginMemberArgumentResolver;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(memberController)
                .setCustomArgumentResolvers(loginMemberArgumentResolver)
                .build();
    }

    @Test
    @DisplayName("전체 회원 조회를 한다.")
    void find_members_success() throws Exception {
        mockMvc.perform(
                        get("/api/users"))
                .andExpect(status().isOk());
        verify(memberService).findAllMembers();
    }

    @Test
    @DisplayName("개별 회원 조회를 한다.")
    void find_member_success() throws Exception {
        //given
        Long id = 1L;

        //when, then
        mockMvc.perform(
                        get("/api/users/{id}", id))
                .andExpect(status().isOk());
        verify(memberService).findMember(id);
    }

    @Test
    @DisplayName("즐겨찾기한 글을 조회한다.")
    void find_favorite_board_success() throws Exception {
        // given
        Member member = createUserWithAdminRole();

        given(loginMemberArgumentResolver.supportsParameter(any())).willReturn(true);
        given(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(member);

        // when, then
        mockMvc.perform(
                        get("/api/users/favorites"))
                .andExpect(status().isOk());
        verify(memberService).findFavorites(member);
    }

    @Test
    @DisplayName("회원 정보를 수정한다.")
    void edit_member_info_success() throws Exception {
        // given
        MemberEditRequestDto memberEditRequestDto = new MemberEditRequestDto("name", "nickname");
        Member member = createUserWithAdminRole();
        given(loginMemberArgumentResolver.supportsParameter(any())).willReturn(true);
        given(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(member);

        // when, then
        mockMvc.perform(
                        put("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(memberEditRequestDto)))
                .andExpect(status().isOk());

        verify(memberService).editMemberInfo(member, memberEditRequestDto);
        assertThat(memberEditRequestDto.getName()).isEqualTo("name");
    }

    @Test
    @DisplayName("회원 탈퇴를 한다.")
    void delete_member_success() throws Exception {
        // given
        Member member = createUserWithAdminRole();

        given(loginMemberArgumentResolver.supportsParameter(any())).willReturn(true);
        given(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(member);

        // when then
        mockMvc.perform(
                        delete("/api/users"))
                .andExpect(status().isOk());

        verify(memberService).deleteMemberInfo(member);
    }
}
