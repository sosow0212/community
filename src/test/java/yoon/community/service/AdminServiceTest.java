package yoon.community.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yoon.community.dto.board.BoardSimpleDto;
import yoon.community.dto.user.UserEditRequestDto;
import yoon.community.entity.board.Board;
import yoon.community.entity.user.User;
import yoon.community.repository.board.BoardRepository;
import yoon.community.repository.report.BoardReportRepository;
import yoon.community.repository.report.UserReportRepository;
import yoon.community.repository.user.UserRepository;
import yoon.community.service.admin.AdminService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static yoon.community.factory.BoardFactory.createBoard;
import static yoon.community.factory.UserFactory.createUser;


@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {
    @InjectMocks
    AdminService adminService;

    @Mock
    UserRepository userRepository;
    @Mock
    BoardRepository boardRepository;
    @Mock
    UserReportRepository userReportRepository;
    @Mock
    BoardReportRepository boardReportRepository;


    @Test
    @DisplayName("manageReportedUser 서비스 테스트")
    void manageReportedUserTest() {
        // given
        List<User> users = new ArrayList<>();
        users.add(createUser());
        given(userRepository.findByReportedIsTrue()).willReturn(users);

        // when
        List<UserEditRequestDto> result = adminService.findReportedUsers();

        // then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("unlockUser 서비스 테스트")
    void unlockUserTest() {
        // given
        User user = createUser();
        user.setReported(true);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        // when
        UserEditRequestDto result = adminService.processUnlockUser(anyLong());

        // then
        verify(userReportRepository).deleteAllByReportedUserId(anyLong());
    }

    @Test
    @DisplayName("manageReportedBoards 서비스 테스트")
    void manageReportedBoardsTest() {
        // given
        List<Board> boards = new ArrayList<>();
        boards.add(createBoard());
        given(boardRepository.findByReportedIsTrue()).willReturn(boards);

        // when
        List<BoardSimpleDto> result = adminService.findReportedBoards();

        // then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("unlockBoard 서비스 테스트")
    void unlockBoardTest() {
        // given
        Board board = createBoard();
        board.setReported(true);
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));

        // when
        BoardSimpleDto result = adminService.processUnlockBoard(anyLong());

        // then
        assertThat(result.getTitle()).isEqualTo("title");
    }
}
