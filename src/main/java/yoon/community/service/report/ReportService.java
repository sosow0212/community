package yoon.community.service.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoon.community.dto.report.BoardReportRequest;
import yoon.community.dto.report.BoardReportResponse;
import yoon.community.dto.report.MemberReportRequestDto;
import yoon.community.dto.report.MemberReportResponseDto;
import yoon.community.dto.member.MemberEditRequestDto;
import yoon.community.entity.board.Board;
import yoon.community.entity.report.BoardReportHistory;
import yoon.community.entity.report.MemberReportHistory;
import yoon.community.entity.member.Member;
import yoon.community.exception.AlreadyReportException;
import yoon.community.exception.BoardNotFoundException;
import yoon.community.exception.MemberNotFoundException;
import yoon.community.exception.NotSelfReportException;
import yoon.community.repository.board.BoardRepository;
import yoon.community.repository.report.BoardReportRepository;
import yoon.community.repository.report.MemberReportRepository;
import yoon.community.repository.member.MemberRepository;

@RequiredArgsConstructor
@Service
public class ReportService {
    private final static int NORMAL_USER_REPORT_LIMIT_FOR_BEING_REPORTED = 3;
    private final static int NORMAL_BOARD_REPORT_LIMIT_FOR_BEING_REPORTED = 10;
    public final BoardReportRepository boardReportHistoryRepository;
    public final MemberReportRepository userReportHistoryRepository;
    public final MemberRepository memberRepository;
    public final BoardRepository boardRepository;

    @Transactional
    public MemberReportResponseDto reportUser(Member reporter, MemberReportRequestDto req) {
        validateUserReportRequest(reporter, req);
        Member reportedMember = memberRepository.findById(req.getReportedUserId()).orElseThrow(MemberNotFoundException::new);
        MemberReportHistory memberReportHistory = createUserReportHistory(reporter, reportedMember, req);
        checkUserStatusIsBeingReported(reportedMember, req);
        return new MemberReportResponseDto(memberReportHistory.getId(), MemberEditRequestDto.toDto(reportedMember),
                req.getContent(),
                memberReportHistory.getCreatedAt());
    }

    private void checkUserStatusIsBeingReported(Member reportedMember, MemberReportRequestDto req) {
        if (userReportHistoryRepository.findByReportedUserId(req.getReportedUserId()).size()
                >= NORMAL_USER_REPORT_LIMIT_FOR_BEING_REPORTED) {
            reportedMember.setStatusIsBeingReported();
        }
    }

    private MemberReportHistory createUserReportHistory(Member reporter, Member reportedMember, MemberReportRequestDto req) {
        MemberReportHistory memberReportHistory = new MemberReportHistory(reporter.getId(), reportedMember.getId(),
                req.getContent());
        userReportHistoryRepository.save(memberReportHistory);
        return memberReportHistory;
    }

    private void validateUserReportRequest(Member reporter, MemberReportRequestDto req) {
        if (reporter.isReportMySelf(req.getReportedUserId())) {
            throw new NotSelfReportException();
        }

        if (userReportHistoryRepository.existsByReporterIdAndReportedUserId(reporter.getId(),
                req.getReportedUserId())) {
            throw new AlreadyReportException();
        }
    }

    @Transactional
    public BoardReportResponse reportBoard(Member reporter, BoardReportRequest req) {
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

    private BoardReportHistory createBoardReportHistory(Member reporter, Board reportedBoard, BoardReportRequest req) {
        BoardReportHistory boardReportHistory = new BoardReportHistory(reporter.getId(), reportedBoard.getId(),
                req.getContent());
        boardReportHistoryRepository.save(boardReportHistory);
        return boardReportHistory;
    }

    private void validateBoard(Member reporter, Board reportedBoard, BoardReportRequest req) {
        if (reporter.isReportMySelf(reportedBoard.getMember().getId())) {
            throw new NotSelfReportException();
        }

        if (boardReportHistoryRepository.existsByReporterIdAndReportedBoardId(reporter.getId(),
                req.getReportedBoardId())) {
            throw new AlreadyReportException();
        }
    }
}
