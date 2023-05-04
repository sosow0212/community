package yoon.community.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yoon.community.domain.board.Board;
import yoon.community.domain.member.Authority;
import yoon.community.domain.member.Member;
import yoon.community.domain.report.BoardReportHistory;
import yoon.community.domain.report.MemberReportHistory;
import yoon.community.dto.report.BoardReportRequest;
import yoon.community.dto.report.BoardReportResponse;
import yoon.community.dto.report.MemberReportRequestDto;
import yoon.community.dto.report.MemberReportResponseDto;
import yoon.community.exception.AlreadyReportException;
import yoon.community.exception.BoardNotFoundException;
import yoon.community.exception.MemberNotFoundException;
import yoon.community.exception.NotSelfReportException;
import yoon.community.repository.board.BoardRepository;
import yoon.community.repository.member.MemberRepository;
import yoon.community.repository.report.BoardReportRepository;
import yoon.community.repository.report.MemberReportRepository;
import yoon.community.service.report.ReportService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static yoon.community.factory.BoardFactory.createBoard;
import static yoon.community.factory.UserFactory.createUser;
import static yoon.community.factory.UserFactory.createUser2;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    @InjectMocks
    ReportService reportService;

    @Mock
    BoardReportRepository boardReportRepository;
    @Mock
    MemberReportRepository memberReportRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    BoardRepository boardRepository;

    @Test
    @DisplayName("유저를 신고한다.")
    void report_member_success() {
        // given
        Member reporter = createUser();
        reporter.setId(1L);
        Member reportedMember = createUser2();
        reportedMember.setId(2L);
        MemberReportRequestDto req = new MemberReportRequestDto(reportedMember.getId(), "별로입니다.");
        MemberReportHistory memberReportHistory = new MemberReportHistory(1L, reporter.getId(), reportedMember.getId(),
                req.getContent());

        given(memberReportRepository.existsByReporterIdAndReportedUserId(reporter.getId(),
                req.getReportedUserId())).willReturn(false);
        given(memberRepository.findById(req.getReportedUserId())).willReturn(Optional.of(reportedMember));
        given(memberReportRepository.findByReportedUserId(req.getReportedUserId())).willReturn(
                List.of(memberReportHistory));

        // when
        MemberReportResponseDto result = reportService.reportUser(reporter, req);

        // then
        assertThat(result.getReportedUser().getName()).isEqualTo(reportedMember.getName());
    }

    @Test
    @DisplayName("올바르지 않은 유저가 신고될 경우 예외를 발생한다.")
    void throws_exception_when_reported_member_invalid() {
        // given
        Member reporter = createUser();
        MemberReportRequestDto req = new MemberReportRequestDto(1L, "신고사유");

        // when & then
        assertThatThrownBy(() -> reportService.reportUser(reporter, req))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("유저가 스스로를 신고할 경우 예외를 발생한다.")
    void throws_exception_when_report_myself() {
        // given
        Member reporter = new Member(1L, "username", "1", "name", "nickname", Authority.ROLE_USER, false);
        MemberReportRequestDto req = new MemberReportRequestDto(1L, "신고사유");

        // when & then
        assertThatThrownBy(() -> reportService.reportUser(reporter, req))
                .isInstanceOf(NotSelfReportException.class);
    }

    @Test
    @DisplayName("이미 신고한 유저를 또 신고할 경우 예외가 발생한다.")
    void throws_exception_when_report_same_member_repeat() {
        // given
        Member reporter = createUser();
        Member reported = new Member(1L, "username", "1", "name", "nickname", Authority.ROLE_USER, false);
        MemberReportRequestDto req = new MemberReportRequestDto(1L, "신고사유");
        given(memberReportRepository.existsByReporterIdAndReportedUserId(reporter.getId(), req.getReportedUserId()))
                .willReturn(true);

        // when & then
        assertThatThrownBy(() -> reportService.reportUser(reporter, req))
                .isInstanceOf(AlreadyReportException.class);
    }

    @Test
    @DisplayName("게시글을 신고한다.")
    void report_board_success() {
        // given
        Member reporter = createUser();
        reporter.setId(1L);
        Member reportedBoardOwner = createUser2();
        reportedBoardOwner.setId(2L);
        Board reportedBoard = createBoard(reportedBoardOwner);
        BoardReportRequest req = new BoardReportRequest(reportedBoard.getId(), "별로입니다.");
        BoardReportHistory boardReportHistory = new BoardReportHistory(1L, reporter.getId(), reportedBoard.getId(),
                "content");

        given(boardRepository.findById(req.getReportedBoardId())).willReturn(Optional.of(reportedBoard));
        given(boardReportRepository.existsByReporterIdAndReportedBoardId(reporter.getId(),
                req.getReportedBoardId())).willReturn(false);
        given(boardReportRepository.findByReportedBoardId(req.getReportedBoardId())).willReturn(
                List.of(boardReportHistory));

        // when
        BoardReportResponse result = reportService.reportBoard(reporter, req);

        // then
        assertThat(result.getReportedBoardId()).isEqualTo(reportedBoard.getId());
    }

    @Test
    @DisplayName("올바르지 않은 게시글이 신고될 경우 예외를 발생한다.")
    void throws_exception_when_reported_board_invalid() {
        // given
        Member reporter = createUser();
        BoardReportRequest req = new BoardReportRequest(1L, "신고사유");

        // when & then
        assertThatThrownBy(() -> reportService.reportBoard(reporter, req))
                .isInstanceOf(BoardNotFoundException.class);
    }

    @Test
    @DisplayName("유저가 자신의 게시글을 신고할 경우 예외를 발생한다.")
    void throws_exception_when_report_board_myself() {
        // given
        Long boardId = 1L;
        Member reporter = createUser();
        Board board = createBoard(reporter);
        board.setId(boardId);
        BoardReportRequest req = new BoardReportRequest(boardId, "신고사유");
        given(boardRepository.findById(req.getReportedBoardId())).willReturn(Optional.of(board));

        // when & then
        assertThatThrownBy(() -> reportService.reportBoard(reporter, req))
                .isInstanceOf(NotSelfReportException.class);
    }

    @Test
    @DisplayName("이미 신고한 게시글을 또 신고할 경우 예외가 발생한다.")
    void throws_exception_when_report_same_board_repeat() {
        // given
        Member reporter = createUser();
        reporter.setId(1L);

        Board board = createBoard(createUser());
        Long boardId = 3L;
        board.setId(boardId);
        BoardReportRequest req = new BoardReportRequest(boardId, "신고사유");

        given(boardRepository.findById(req.getReportedBoardId())).willReturn(Optional.of(board));
        given(boardReportRepository.existsByReporterIdAndReportedBoardId(reporter.getId(), req.getReportedBoardId()))
                .willReturn(true);

        // when & then
        assertThatThrownBy(() -> reportService.reportBoard(reporter, req))
                .isInstanceOf(AlreadyReportException.class);
    }
}
