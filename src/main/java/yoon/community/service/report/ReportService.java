package yoon.community.service.report;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoon.community.dto.report.BoardReportRequest;
import yoon.community.dto.report.BoardReportResponse;
import yoon.community.dto.report.UserReportRequest;
import yoon.community.dto.report.UserReportResponse;
import yoon.community.dto.user.UserDto;
import yoon.community.entity.board.Board;
import yoon.community.entity.report.BoardReport;
import yoon.community.entity.report.UserReport;
import yoon.community.entity.user.User;
import yoon.community.exception.AlreadyReportException;
import yoon.community.exception.BoardNotFoundException;
import yoon.community.exception.MemberNotFoundException;
import yoon.community.exception.type.NotSelfReportException;
import yoon.community.repository.board.BoardRepository;
import yoon.community.repository.report.BoardReportRepository;
import yoon.community.repository.report.UserReportRepository;
import yoon.community.repository.user.UserRepository;

@RequiredArgsConstructor
@Service
public class ReportService {

    public final BoardReportRepository boardReportRepository;
    public final UserReportRepository userReportRepository;
    public final UserRepository userRepository;
    public final BoardRepository boardRepository;

    @Transactional
    public UserReportResponse reportUser(UserReportRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User reporter = userRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        User reportedUser = userRepository.findById(req.getReportedUserId()).orElseThrow(MemberNotFoundException::new);

        if (reporter.getId() == req.getReportedUserId()) {
            // 자기 자신을 신고한 경우
            throw new NotSelfReportException();
        }

        if (userReportRepository.findByReporterIdAndReportedUserId(reporter.getId(), req.getReportedUserId()) == null) {
            // 신고 한 적이 없다면, 테이블 생성 후 신고 처리 (ReportedUser의 User테이블 boolean 값 true 변경 ==> 신고처리)
            UserReport userReport = new UserReport(reporter.getId(), reportedUser.getId(), req.getContent());
            userReportRepository.save(userReport);

            if (userReportRepository.findByReportedUserId(req.getReportedUserId()).size() >= 10) {
                // 신고 수 10 이상일 시 true 설정
                reportedUser.setReported(true);
            }

            UserReportResponse res = new UserReportResponse(userReport.getId(), UserDto.toDto(reportedUser), req.getContent(), userReport.getCreateDate());
            return res;
        } else {
            // 이미 신고를 했다면 리턴
            throw new AlreadyReportException();
        }
    }

    @Transactional
    public BoardReportResponse reportBoard(BoardReportRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User reporter = userRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        Board reportedBoard = boardRepository.findById(req.getReportedBoardId()).orElseThrow(BoardNotFoundException::new);

        if (reporter.getId() == reportedBoard.getUser().getId()) {
            throw new NotSelfReportException();
        }

        if (boardReportRepository.findByReporterIdAndReportedBoardId(reporter.getId(), req.getReportedBoardId()) == null) {
            // 신고 한 적이 없다면, 테이블 생성 후 신고 처리
            BoardReport boardReport = new BoardReport(reporter.getId(), reportedBoard.getId(), req.getContent());
            boardReportRepository.save(boardReport);


            if (boardReportRepository.findByReportedBoardId(req.getReportedBoardId()).size() >= 10) {
                // 신고 수 10 이상일 시 true 설정
                reportedBoard.setReported(true);
            }

            BoardReportResponse res = new BoardReportResponse(boardReport.getId(), req.getReportedBoardId(), req.getContent(), boardReport.getCreateDate());
            return res;
        } else {
            throw new AlreadyReportException();
        }
    }


}
