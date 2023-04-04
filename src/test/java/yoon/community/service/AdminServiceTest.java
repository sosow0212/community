package yoon.community.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static yoon.community.factory.BoardFactory.createBoard;
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
import yoon.community.domain.board.Board;
import yoon.community.domain.member.Member;
import yoon.community.dto.board.BoardSimpleDto;
import yoon.community.dto.member.MemberSimpleNicknameResponseDto;
import yoon.community.exception.BoardNotReportedException;
import yoon.community.repository.board.BoardRepository;
import yoon.community.repository.member.MemberRepository;
import yoon.community.repository.report.BoardReportRepository;
import yoon.community.repository.report.MemberReportRepository;
import yoon.community.service.admin.AdminService;


@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @InjectMocks
    AdminService adminService;

    @Mock
    MemberRepository memberRepository;
    @Mock
    BoardRepository boardRepository;
    @Mock
    BoardReportRepository boardReportRepository;
    @Mock
    MemberReportRepository memberReportRepository;

    @Test
    @DisplayName("신고된 유저를 찾는다.")
    void find_reported_users_success() {
        // given
        List<Member> members = new ArrayList<>();
        members.add(createUser());
        given(memberRepository.findByReportedIsTrue()).willReturn(members);

        // when
        List<MemberSimpleNicknameResponseDto> result = adminService.findReportedUsers();

        // then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("유저 신고처리를 풀어준다.")
    void unlock_reported_user_success() {
        // given
        Member member = createUser();
        member.makeStatusReported();
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        // when
        MemberSimpleNicknameResponseDto result = adminService.processUnlockUser(anyLong());

        // then
        verify(memberReportRepository).deleteAllByReportedUserId(anyLong());
    }

    @Test
    @DisplayName("신고되지 않은 유저를 해제할시 예외가 발생한다.")
    void throws_exception_when_unlock_not_reported_user() {
        // given
        Member member = createUser();
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        // when & then
        assertThatThrownBy(() -> adminService.processUnlockUser(anyLong()))
                .isInstanceOf(BoardNotReportedException.class);
    }

    @Test
    @DisplayName("신고된 게시글을 찾는다.")
    void find_reported_boards_success() {
        // given
        List<Board> boards = new ArrayList<>();
        boards.add(createBoard());
        given(boardRepository.findByReportedIsTrue()).willReturn(boards);

        // when
        List<BoardSimpleDto> result = adminService.findReportedBoards();

        // then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("신고된 게시물을 풀어준다.")
    void unlock_reported_board_success() {
        // given
        Board board = createBoard();
        board.setId(1L);
        board.makeStatusReported();
        given(boardRepository.findById(1L)).willReturn(Optional.of(board));

        // when
        BoardSimpleDto result = adminService.processUnlockBoard(1L);

        // then
        assertThat(result.getTitle()).isEqualTo("title");
    }

    @Test
    @DisplayName("신고되지 않은 게시글을 해제할시 예외가 발생한다.")
    void throws_exception_when_unlock_not_reported_board() {
        // given
        Board board = createBoard();
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));

        // when & then
        assertThatThrownBy(() -> adminService.processUnlockBoard(anyLong()))
                .isInstanceOf(BoardNotReportedException.class);
     }
}
