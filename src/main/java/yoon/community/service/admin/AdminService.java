package yoon.community.service.admin;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoon.community.dto.board.BoardSimpleDto;
import yoon.community.dto.member.MemberEditRequestDto;
import yoon.community.domain.board.Board;
import yoon.community.domain.member.Member;
import yoon.community.dto.member.MemberSimpleNicknameResponseDto;
import yoon.community.exception.BoardNotFoundException;
import yoon.community.exception.MemberNotEqualsException;
import yoon.community.exception.NotReportedException;
import yoon.community.repository.board.BoardRepository;
import yoon.community.repository.report.BoardReportRepository;
import yoon.community.repository.report.MemberReportRepository;
import yoon.community.repository.member.MemberRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final MemberReportRepository memberReportRepository;
    private final BoardReportRepository boardReportRepository;

    @Transactional(readOnly = true)
    public List<MemberSimpleNicknameResponseDto> findReportedUsers() {
        List<Member> members = memberRepository.findByReportedIsTrue();
        return members.stream()
                .map(user -> new MemberSimpleNicknameResponseDto().toDto(user))
                .collect(Collectors.toList());
    }

    @Transactional
    public MemberSimpleNicknameResponseDto processUnlockUser(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(MemberNotEqualsException::new);
        validateUnlockUser(member);
        deleteUnlockUser(member, id);
        return MemberSimpleNicknameResponseDto.toDto(member);
    }

    private void validateUnlockUser(Member member) {
        if (!member.isReported()) {
            throw new NotReportedException();
        }
    }

    private void deleteUnlockUser(Member member, Long id) {
        member.unlockReport();
        memberReportRepository.deleteAllByReportedUserId(id);
    }


    @Transactional(readOnly = true)
    public List<BoardSimpleDto> findReportedBoards() {
        List<Board> boards = boardRepository.findByReportedIsTrue();
        return boards.stream()
                .map(board -> new BoardSimpleDto().toDto(board))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BoardSimpleDto processUnlockBoard(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        validateUnlockBoard(board);
        deleteUnlockBoard(board, id);
        return new BoardSimpleDto().toDto(board);
    }

    private void deleteUnlockBoard(Board board, Long id) {
        board.unReportedBoard();
        boardReportRepository.deleteAllByReportedBoardId(id);
    }

    private void validateUnlockBoard(Board board) {
        if (!board.isReported()) {
            throw new NotReportedException();
        }
    }
}
