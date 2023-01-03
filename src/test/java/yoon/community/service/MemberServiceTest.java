package yoon.community.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yoon.community.dto.member.MemberEditRequestDto;
import yoon.community.dto.member.MemberSimpleResponseDto;
import yoon.community.entity.member.Authority;
import yoon.community.entity.member.Member;
import yoon.community.exception.MemberNotFoundException;
import yoon.community.repository.member.MemberRepository;
import yoon.community.service.member.MemberService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static yoon.community.factory.UserFactory.createUser;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    @Test
    @DisplayName("findUser() 서비스 테스트")
    void findUserTest() {
        // given
        Member member = new Member("yoon", "1234", Authority.ROLE_USER);
        member.setName("yoon");
        member.setNickname("yoon");

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        // when
        MemberSimpleResponseDto result = memberService.findMember(1L);

        // then
        assertThat(result.getName()).isEqualTo(member.getName());
    }

    @Test
    @DisplayName("MemberNotFoundException 테스트")
    void memberNotFoundExceptionTest() {
        // given
        given(memberRepository.findById(any())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> memberService.findMember(1L)).isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("deleteUserInfo() 서비스 테스트")
    void deleteUserInfoTest() {
        // given
        Member member = createUser();

        // when
        memberService.deleteMemberInfo(member);

        // then
        verify(memberRepository).delete(member);

    }
}
