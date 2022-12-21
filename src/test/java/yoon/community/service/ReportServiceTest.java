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
import yoon.community.dto.report.UserReportRequest;
import yoon.community.dto.report.UserReportResponse;
import yoon.community.entity.board.Board;
import yoon.community.entity.report.BoardReportHistory;
import yoon.community.entity.report.UserReportHistory;
import yoon.community.entity.user.Authority;
import yoon.community.entity.user.User;
import yoon.community.repository.board.BoardRepository;
import yoon.community.repository.report.BoardReportRepository;
import yoon.community.repository.report.UserReportRepository;
import yoon.community.repository.user.UserRepository;
import yoon.community.service.report.ReportService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
    UserReportRepository userReportRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    BoardRepository boardRepository;

    @BeforeEach

    @Test
    @DisplayName("reportUser 서비스 테스트")
    void reportUserTest() {
        // given
        User reporter = createUser();
        reporter.setId(1);
        User reportedUser = createUser2();
        reportedUser.setId(2);
        UserReportRequest req = new UserReportRequest(reportedUser.getId(), "별로입니다.");
        UserReportHistory userReportHistory = new UserReportHistory(1, reporter.getId(), reportedUser.getId(),
                req.getContent());

        given(userReportRepository.existsByReporterIdAndReportedUserId(reporter.getId(),
                req.getReportedUserId())).willReturn(false);
        given(userRepository.findById(req.getReportedUserId())).willReturn(Optional.of(reportedUser));
        given(userReportRepository.findByReportedUserId(req.getReportedUserId())).willReturn(
                List.of(userReportHistory));

        // when
        UserReportResponse result = reportService.reportUser(reporter, req);

        // then
        assertThat(result.getReportedUser().getName()).isEqualTo(reportedUser.getName());

    }


    @Test
    @DisplayName("reportBoard 서비스 테스트")
    void reportBoardTest() {
        // given
        User reporter = createUser();
        reporter.setId(1);
        User reportedBoardOwner = createUser2();
        reportedBoardOwner.setId(2);
        Board reportedBoard = createBoard(reportedBoardOwner);
        BoardReportRequest req = new BoardReportRequest(reportedBoard.getId(), "별로입니다.");
        BoardReportHistory boardReportHistory = new BoardReportHistory(1, reporter.getId(), reportedBoard.getId(),
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
