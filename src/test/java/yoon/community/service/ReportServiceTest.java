package yoon.community.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yoon.community.dto.report.BoardReportRequest;
import yoon.community.dto.report.BoardReportResponse;
import yoon.community.dto.report.MemberReportRequestDto;
import yoon.community.dto.report.MemberReportResponseDto;
import yoon.community.entity.board.Board;
import yoon.community.entity.report.BoardReportHistory;
import yoon.community.entity.report.MemberReportHistory;
import yoon.community.entity.member.Member;
import yoon.community.repository.board.BoardRepository;
import yoon.community.repository.report.BoardReportRepository;
import yoon.community.repository.report.MemberReportRepository;
import yoon.community.repository.member.MemberRepository;
import yoon.community.service.report.ReportService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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

    @BeforeEach

    @Test
    @DisplayName("reportUser 서비스 테스트")
    void reportUserTest() {
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
    @DisplayName("reportBoard 서비스 테스트")
    void reportBoardTest() {
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
}
