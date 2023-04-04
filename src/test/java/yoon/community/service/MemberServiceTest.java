package yoon.community.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static yoon.community.factory.UserFactory.createUser;

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
import yoon.community.dto.board.BoardSimpleDto;
import yoon.community.dto.member.MemberEditRequestDto;
import yoon.community.dto.member.MemberSimpleResponseDto;
import yoon.community.exception.MemberNotFoundException;
import yoon.community.repository.board.FavoriteRepository;
import yoon.community.repository.member.MemberRepository;
import yoon.community.service.member.MemberService;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    FavoriteRepository favoriteRepository;

    @Test
    @DisplayName("유저를 찾는다.")
    void find_user_success() {
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
    @DisplayName("없는 유저라면 예외를 발생한다.")
    void throws_exception_when_member_not_found() {
        // given
        given(memberRepository.findById(any())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> memberService.findMember(1L))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("유저 정보 삭제를 한다.")
    void delete_user_success() {
        // given
        Member member = createUser();

        // when
        memberService.deleteMemberInfo(member);

        // then
        verify(memberRepository).delete(member);
    }

    @Test
    @DisplayName("유저 정보를 수정한다.")
    void edit_member_success() {
        // given
        Member member = createUser();
        MemberEditRequestDto req = new MemberEditRequestDto("수정", "수정");

        // when
        Member result = memberService.editMemberInfo(member, req);

        // then
        assertThat(result.getName()).isEqualTo(req.getName());
    }

    @Test
    @DisplayName("좋아요 처리한 게시글을 찾는다.")
    void find_favorite_board_success() {
        // given
        Member member = createUser();
        given(favoriteRepository.findAllByMember(member)).willReturn(new ArrayList<>());

        // when
        List<BoardSimpleDto> result = memberService.findFavorites(member);

        // then
        assertThat(result).isEmpty();
    }
}
