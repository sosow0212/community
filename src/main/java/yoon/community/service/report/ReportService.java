package yoon.community.service.report;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoon.community.domain.board.Board;
import yoon.community.domain.member.Member;
import yoon.community.domain.report.BoardReportHistory;
import yoon.community.domain.report.MemberReportHistory;
import yoon.community.dto.member.MemberEditRequestDto;
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

@Service
public class ReportService {

    private static final int NORMAL_USER_REPORT_LIMIT_FOR_BEING_REPORTED = 3;
    private static final int NORMAL_BOARD_REPORT_LIMIT_FOR_BEING_REPORTED = 10;

    public final BoardReportRepository boardReportHistoryRepository;
    public final MemberReportRepository MemberReportHistoryRepository;
    public final MemberRepository memberRepository;
    public final BoardRepository boardRepository;

    public ReportService(final BoardReportRepository boardReportHistoryRepository, final MemberReportRepository memberReportHistoryRepository, final MemberRepository memberRepository, final BoardRepository boardRepository) {
        this.boardReportHistoryRepository = boardReportHistoryRepository;
        MemberReportHistoryRepository = memberReportHistoryRepository;
        this.memberRepository = memberRepository;
        this.boardRepository = boardRepository;
    }

    @Transactional
    public MemberReportResponseDto reportUser(final Member reporter, final MemberReportRequestDto req) {
        validateUserReportRequest(reporter, req);

        Member reportedMember = memberRepository.findById(req.getReportedUserId())
                .orElseThrow(MemberNotFoundException::new);

        MemberReportHistory memberReportHistory = createUserReportHistory(reporter, reportedMember, req);
        checkUserStatusIsBeingReported(reportedMember, req);

        return MemberReportResponseDto.toDto(memberReportHistory, MemberEditRequestDto.toDto(reportedMember), req);
    }

    private void validateUserReportRequest(final Member reporter, final MemberReportRequestDto req) {
        if (reporter.isSameMemberId(req.getReportedUserId())) {
            throw new NotSelfReportException();
        }

        if (MemberReportHistoryRepository.existsByReporterIdAndReportedUserId(reporter.getId(),
                req.getReportedUserId())) {
            throw new AlreadyReportException();
        }
    }

    private void checkUserStatusIsBeingReported(final Member reportedMember, final MemberReportRequestDto req) {
        if (MemberReportHistoryRepository.findByReportedUserId(req.getReportedUserId()).size()
                >= NORMAL_USER_REPORT_LIMIT_FOR_BEING_REPORTED) {
            reportedMember.makeStatusReported();
        }
    }

    private MemberReportHistory createUserReportHistory(final Member reporter, final Member reportedMember,
                                                        final MemberReportRequestDto req) {
        MemberReportHistory memberReportHistory = new MemberReportHistory(reporter.getId(), reportedMember.getId(),
                req.getContent());
        MemberReportHistoryRepository.save(memberReportHistory);
        return memberReportHistory;
    }

    @Transactional
    public BoardReportResponse reportBoard(final Member reporter, final BoardReportRequest req) {
        Board reportedBoard = boardRepository.findById(req.getReportedBoardId())
                .orElseThrow(BoardNotFoundException::new);

        validateBoard(reporter, reportedBoard, req);
        BoardReportHistory boardReportHistory = createBoardReportHistory(reporter, reportedBoard, req);
        checkBoardStatusIsBeingReported(reportedBoard, req);

        return new BoardReportResponse(boardReportHistory.getId(), req.getReportedBoardId(),
                req.getContent(), boardReportHistory.getCreatedAt());
    }

    private void validateBoard(final Member reporter, final Board reportedBoard, final BoardReportRequest req) {
        if (reporter.isSameMemberId(reportedBoard.getMember().getId())) {
            throw new NotSelfReportException();
        }

        if (boardReportHistoryRepository.existsByReporterIdAndReportedBoardId(reporter.getId(),
                req.getReportedBoardId())) {
            throw new AlreadyReportException();
        }
    }

    private void checkBoardStatusIsBeingReported(final Board reportedBoard, final BoardReportRequest req) {
        if (boardReportHistoryRepository.findByReportedBoardId(req.getReportedBoardId()).size()
                >= NORMAL_BOARD_REPORT_LIMIT_FOR_BEING_REPORTED) {
            reportedBoard.makeStatusReported();
        }
    }

    private BoardReportHistory createBoardReportHistory(final Member reporter, final Board reportedBoard, final BoardReportRequest req) {
        BoardReportHistory boardReportHistory = new BoardReportHistory(reporter.getId(), reportedBoard.getId(),
                req.getContent());

        boardReportHistoryRepository.save(boardReportHistory);
        return boardReportHistory;
    }
}
