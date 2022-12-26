package yoon.community.service.admin;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoon.community.dto.board.BoardSimpleDto;
import yoon.community.dto.user.UserEditRequestDto;
import yoon.community.entity.board.Board;
import yoon.community.entity.user.User;
import yoon.community.exception.BoardNotFoundException;
import yoon.community.exception.MemberNotEqualsException;
import yoon.community.exception.NotReportedException;
import yoon.community.repository.board.BoardRepository;
import yoon.community.repository.report.BoardReportRepository;
import yoon.community.repository.report.UserReportRepository;
import yoon.community.repository.user.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final UserReportRepository userReportRepository;
    private final BoardReportRepository boardReportRepository;

    @Transactional(readOnly = true)
    public List<UserEditRequestDto> findReportedUsers() {
        List<User> users = userRepository.findByReportedIsTrue();
        List<UserEditRequestDto> usersDto = users.stream()
                .map(user -> new UserEditRequestDto().toDto(user))
                .collect(Collectors.toList());
        return usersDto;
    }

    @Transactional
    public UserEditRequestDto processUnlockUser(int id) {
        User user = userRepository.findById(id).orElseThrow(MemberNotEqualsException::new);
        validateUnlockUser(user);
        deleteUnlockUser(user, id);
        return UserEditRequestDto.toDto(user);
    }

    private void validateUnlockUser(User user) {
        if (!user.isReported()) {
            throw new NotReportedException();
        }
    }

    private void deleteUnlockUser(User user, int id) {
        user.unlockReport();
        userReportRepository.deleteAllByReportedUserId(id);
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
    public BoardSimpleDto processUnlockBoard(int id) {
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        validateUnlockBoard(board);
        deleteUnlockBoard(board, id);
        return new BoardSimpleDto().toDto(board);
    }

    private void deleteUnlockBoard(Board board, int id) {
        board.unReportedBoard();
        boardReportRepository.deleteAllByReportedBoardId(id);
    }

    private void validateUnlockBoard(Board board) {
        if (!board.isReported()) {
            throw new NotReportedException();
        }
    }
}
