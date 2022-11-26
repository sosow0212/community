package yoon.community.service.admin;

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

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final UserReportRepository userReportRepository;
    private final BoardReportRepository boardReportRepository;

    @Transactional(readOnly = true)
    public List<UserEditRequestDto> manageReportedUser() {
        List<User> users = userRepository.findByReportedIsTrue();
        List<UserEditRequestDto> usersDto = new ArrayList<>();
        users.stream().forEach(i -> usersDto.add(new UserEditRequestDto().toDto(i)));
        return usersDto;
    }

    @Transactional
    public UserEditRequestDto unlockUser(int id) {
        User user = userRepository.findById(id).orElseThrow(MemberNotEqualsException::new);
        if(!user.isReported()) {
            throw new NotReportedException();
        }
        user.setReported(false);
        userReportRepository.deleteAllByReportedUserId(id);
        return UserEditRequestDto.toDto(user);
    }


    @Transactional(readOnly = true)
    public List<BoardSimpleDto> manageReportedBoards() {
        List<Board> boards = boardRepository.findByReportedIsTrue();
        List<BoardSimpleDto> boardsDto = new ArrayList<>();
        boards.stream().forEach(i -> boardsDto.add(new BoardSimpleDto().toDto(i)));
        return boardsDto;
    }

    @Transactional(readOnly = true)
    public BoardSimpleDto unlockBoard(int id) {
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        if(!board.isReported()) {
            throw new NotReportedException();
        }
        board.setReported(false);
        boardReportRepository.deleteAllByReportedBoardId(id);
        return new BoardSimpleDto().toDto(board);
    }
}
