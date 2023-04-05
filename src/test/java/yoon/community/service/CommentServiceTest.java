package yoon.community.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static yoon.community.factory.BoardFactory.createBoard;
import static yoon.community.factory.CommentFactory.createComment;
import static yoon.community.factory.UserFactory.createUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yoon.community.domain.board.Board;
import yoon.community.domain.comment.Comment;
import yoon.community.domain.member.Member;
import yoon.community.dto.comment.CommentCreateRequest;
import yoon.community.dto.comment.CommentDto;
import yoon.community.dto.comment.CommentReadCondition;
import yoon.community.exception.BoardNotFoundException;
import yoon.community.exception.CommentNotFoundException;
import yoon.community.exception.MemberNotEqualsException;
import yoon.community.repository.board.BoardRepository;
import yoon.community.repository.commnet.CommentRepository;
import yoon.community.repository.member.MemberRepository;
import yoon.community.service.comment.CommentService;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @InjectMocks
    CommentService commentService;

    @Mock
    CommentRepository commentRepository;

    @Mock
    BoardRepository boardRepository;


    @Test
    @DisplayName("댓글을 모두 찾는다.")
    void find_comments_success() {
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
    @DisplayName("댓글을 생성한다.")
    void create_comment_success() {
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
    @DisplayName("게시글이 없는 경우 댓글 생성에 실패한다.")
    void create_comment_fail_when_board_not_found() {
        // given
        Board board = createBoard();
        CommentCreateRequest req = new CommentCreateRequest(board.getId(), "content");

        // when & then
        assertThatThrownBy(() -> commentService.createComment(req, createUser()))
                .isInstanceOf(BoardNotFoundException.class);
     }

    @Test
    @DisplayName("댓글을 삭제한다.")
    void delete_comment_success() {
        // given
        Member member = createUser();
        Comment comment = createComment(member);
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));

        // when
        commentService.deleteComment(anyLong(), member);

        // then
        verify(commentRepository).delete(any());
    }

    @Test
    @DisplayName("댓글을 찾을 수 없으면 삭제에 실패한다.")
    void delete_comment_fail_when_comment_not_found() {
        // given
        Member member = createUser();

        // when & then
        assertThatThrownBy(() -> commentService.deleteComment(anyLong(), member))
                .isInstanceOf(CommentNotFoundException.class);

    }

    @Test
    @DisplayName("댓글의 주인이 아니면 삭제에 실패한다.")
    void delete_comment_fail_when_comment_is_not_mine() {
        // given
        Member member = createUser();
        Comment comment = createComment(member);
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));

        // when & then
        assertThatThrownBy(() -> commentService.deleteComment(anyLong(), createUser()))
                .isInstanceOf(MemberNotEqualsException.class);
    }
}
