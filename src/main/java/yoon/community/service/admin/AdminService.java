package yoon.community.service.admin;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoon.community.dto.board.BoardSimpleDto;
import yoon.community.dto.member.MemberEditRequestDto;
import yoon.community.entity.board.Board;
import yoon.community.entity.member.Member;
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
    public List<MemberEditRequestDto> findReportedUsers() {
        List<Member> members = memberRepository.findByReportedIsTrue();
        List<MemberEditRequestDto> usersDto = members.stream()
                .map(user -> new MemberEditRequestDto().toDto(user))
                .collect(Collectors.toList());
        return usersDto;
    }

    @Transactional
    public MemberEditRequestDto processUnlockUser(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(MemberNotEqualsException::new);
        validateUnlockUser(member);
        deleteUnlockUser(member, id);
        return MemberEditRequestDto.toDto(member);
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
        List<BoardSimpleDto> boardsDto = boards.stream()
                .map(board -> new BoardSimpleDto().toDto(board))
                .collect(Collectors.toList());
        return boardsDto;
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
