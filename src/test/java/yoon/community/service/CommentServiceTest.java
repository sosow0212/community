package yoon.community.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yoon.community.dto.comment.CommentCreateRequest;
import yoon.community.dto.comment.CommentDto;
import yoon.community.dto.comment.CommentReadCondition;
import yoon.community.entity.board.Board;
import yoon.community.entity.comment.Comment;
import yoon.community.entity.user.User;
import yoon.community.repository.board.BoardRepository;
import yoon.community.repository.commnet.CommentRepository;
import yoon.community.repository.user.UserRepository;
import yoon.community.service.comment.CommentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static yoon.community.factory.BoardFactory.createBoard;
import static yoon.community.factory.CommentFactory.createComment;
import static yoon.community.factory.UserFactory.createUser;


@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @InjectMocks
    CommentService commentService;

    @Mock
    CommentRepository commentRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    BoardRepository boardRepository;


    @Test
    @DisplayName("findAll 서비스 테스트")
    void findAllTest() {
        // given
        List<Comment> commentList = new ArrayList<>();
        commentList.add(createComment());
        CommentReadCondition commentReadCondition = new CommentReadCondition(anyLong());
        given(commentRepository.findByBoardId(commentReadCondition.getBoardId())).willReturn(commentList);

        // when
        List<CommentDto> result = commentService.findAllComments(commentReadCondition);

        // then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("create 서비스 테스트")
    void createTest() {
        // given
        Board board = createBoard();
        board.setId(1L);
        CommentCreateRequest req = new CommentCreateRequest(board.getId(), "content");
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));

        // when
        CommentDto result = commentService.createComment(req, createUser());

        // then
        assertThat(result.getContent()).isEqualTo(req.getContent());
    }

    @Test
    @DisplayName("delete 서비스 테스트")
    void deleteTest() {
        // given
        User user = createUser();
        Comment comment = createComment(user);
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));

        // when
        commentService.deleteComment(anyLong(), user);

        // then
        verify(commentRepository).delete(any());
    }
}
