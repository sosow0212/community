package yoon.community.service.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoon.community.dto.report.BoardReportRequest;
import yoon.community.dto.report.BoardReportResponse;
import yoon.community.dto.report.UserReportRequest;
import yoon.community.dto.report.UserReportResponse;
import yoon.community.dto.user.UserEditRequestDto;
import yoon.community.entity.board.Board;
import yoon.community.entity.report.BoardReportHistory;
import yoon.community.entity.report.UserReportHistory;
import yoon.community.entity.user.User;
import yoon.community.exception.AlreadyReportException;
import yoon.community.exception.BoardNotFoundException;
import yoon.community.exception.MemberNotFoundException;
import yoon.community.exception.NotSelfReportException;
import yoon.community.repository.board.BoardRepository;
import yoon.community.repository.report.BoardReportRepository;
import yoon.community.repository.report.UserReportRepository;
import yoon.community.repository.user.UserRepository;

@RequiredArgsConstructor
@Service
public class ReportService {
    private final static int NORMAL_USER_REPORT_LIMIT_FOR_BEING_REPORTED = 3;
    private final static int NORMAL_BOARD_REPORT_LIMIT_FOR_BEING_REPORTED = 10;
    public final BoardReportRepository boardReportHistoryRepository;
    public final UserReportRepository userReportHistoryRepository;
    public final UserRepository userRepository;
    public final BoardRepository boardRepository;

    @Transactional
    public UserReportResponse reportUser(User reporter, UserReportRequest req) {
        validateUserReportRequest(reporter, req);
        User reportedUser = userRepository.findById(req.getReportedUserId()).orElseThrow(MemberNotFoundException::new);
        UserReportHistory userReportHistory = createUserReportHistory(reporter, reportedUser, req);
        checkUserStatusIsBeingReported(reportedUser, req);
        return new UserReportResponse(userReportHistory.getId(), UserEditRequestDto.toDto(reportedUser),
                req.getContent(),
                userReportHistory.getCreatedAt());
    }

    private void checkUserStatusIsBeingReported(User reportedUser, UserReportRequest req) {
        if (userReportHistoryRepository.findByReportedUserId(req.getReportedUserId()).size()
                >= NORMAL_USER_REPORT_LIMIT_FOR_BEING_REPORTED) {
            reportedUser.setStatusIsBeingReported();
        }
    }

    private UserReportHistory createUserReportHistory(User reporter, User reportedUser, UserReportRequest req) {
        UserReportHistory userReportHistory = new UserReportHistory(reporter.getId(), reportedUser.getId(),
                req.getContent());
        userReportHistoryRepository.save(userReportHistory);
        return userReportHistory;
    }

    private void validateUserReportRequest(User reporter, UserReportRequest req) {
        if (reporter.isReportMySelf(req.getReportedUserId())) {
            throw new NotSelfReportException();
        }

        if (userReportHistoryRepository.existsByReporterIdAndReportedUserId(reporter.getId(),
                req.getReportedUserId())) {
            throw new AlreadyReportException();
        }
    }

    @Transactional
    public BoardReportResponse reportBoard(User reporter, BoardReportRequest req) {
        Board reportedBoard = boardRepository.findById(req.getReportedBoardId())
                .orElseThrow(BoardNotFoundException::new);
        validateBoard(reporter, reportedBoard, req);
        BoardReportHistory boardReportHistory = createBoardReportHistory(reporter, reportedBoard, req);
        checkBoardStatusIsBeingReported(reportedBoard, req);
        return new BoardReportResponse(boardReportHistory.getId(), req.getReportedBoardId(),
                req.getContent(), boardReportHistory.getCreatedAt());
    }

    private void checkBoardStatusIsBeingReported(Board reportedBoard, BoardReportRequest req) {
        if (boardReportHistoryRepository.findByReportedBoardId(req.getReportedBoardId()).size()
                >= NORMAL_BOARD_REPORT_LIMIT_FOR_BEING_REPORTED) {
            reportedBoard.setStatusIsBeingReported();
        }
    }

    private BoardReportHistory createBoardReportHistory(User reporter, Board reportedBoard, BoardReportRequest req) {
        BoardReportHistory boardReportHistory = new BoardReportHistory(reporter.getId(), reportedBoard.getId(),
                req.getContent());
        boardReportHistoryRepository.save(boardReportHistory);
        return boardReportHistory;
    }

    private void validateBoard(User reporter, Board reportedBoard, BoardReportRequest req) {
        if (reporter.isReportMySelf(reportedBoard.getUser().getId())) {
            throw new NotSelfReportException();
        }

        if (boardReportHistoryRepository.existsByReporterIdAndReportedBoardId(reporter.getId(),
                req.getReportedBoardId())) {
            throw new AlreadyReportException();
        }
    }
}
