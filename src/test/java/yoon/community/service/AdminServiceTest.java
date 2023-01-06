package yoon.community.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yoon.community.dto.board.BoardSimpleDto;
import yoon.community.dto.member.MemberEditRequestDto;
import yoon.community.domain.board.Board;
import yoon.community.domain.member.Member;
import yoon.community.repository.board.BoardRepository;
import yoon.community.repository.report.BoardReportRepository;
import yoon.community.repository.report.MemberReportRepository;
import yoon.community.repository.member.MemberRepository;
import yoon.community.service.admin.AdminService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static yoon.community.factory.BoardFactory.createBoard;
import static yoon.community.factory.UserFactory.createUser;


@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {
    @InjectMocks
    AdminService adminService;

    @Mock
    MemberRepository memberRepository;
    @Mock
    BoardRepository boardRepository;
    @Mock
    MemberReportRepository memberReportRepository;
    @Mock
    BoardReportRepository boardReportRepository;


    @Test
    @DisplayName("manageReportedUser 서비스 테스트")
    void manageReportedUserTest() {
        // given
        List<Member> members = new ArrayList<>();
        members.add(createUser());
        given(memberRepository.findByReportedIsTrue()).willReturn(members);

        // when
        List<MemberEditRequestDto> result = adminService.findReportedUsers();

        // then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("unlockUser 서비스 테스트")
    void unlockUserTest() {
        // given
        Member member = createUser();
        member.setReported(true);
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        // when
        MemberEditRequestDto result = adminService.processUnlockUser(anyLong());

        // then
        verify(memberReportRepository).deleteAllByReportedUserId(anyLong());
    }

    @Test
    @DisplayName("manageReportedBoards 서비스 테스트")
    void manageReportedBoardsTest() {
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
    @DisplayName("unlockBoard 서비스 테스트")
    void unlockBoardTest() {
        // given
        Board board = createBoard();
        board.setReported(true);
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));

        // when
        BoardSimpleDto result = adminService.processUnlockBoard(anyLong());

        // then
        assertThat(result.getTitle()).isEqualTo("title");
    }
}
