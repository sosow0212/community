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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
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

        // 매개변수 만들고
        User reporter = createUser();
        User reported = new User("reported", "password", Authority.ROLE_ADMIN);
        UserReportRequest req = new UserReportRequest(1, "content");

        // 로직 내부를 given으로 처리
        given(userRepository.findById(anyInt())).willReturn(Optional.of(reported));

        // when
        UserReportResponse result = reportService.reportUser(reporter, req);

        // then
//        assertThat(result.getReportedUser().getUsername()).isEqualTo("reported");
    }


    @Test
    @DisplayName("reportBoard 서비스 테스트")
    void reportBoardTest() {
        // given
        User reporter = createUser();
        User boardWriter = createUser2();
        reporter.setId(1);
        boardWriter.setId(2);
        Board reported = new Board("title", "content", boardWriter,null, List.of());
        BoardReportRequest req = new BoardReportRequest(1, "content");
        given(boardRepository.findById(anyInt())).willReturn(Optional.of(reported));

        // when
        BoardReportResponse result = reportService.reportBoard(reporter, req);

        // then
        assertThat(result.getContent()).isEqualTo("content");
    }
}
