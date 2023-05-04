package yoon.community.service.comment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public CommentService(final CommentRepository commentRepository, final BoardRepository boardRepository) {
        this.commentRepository = commentRepository;
        this.boardRepository = boardRepository;
    }

    @Transactional(readOnly = true)
    public List<CommentDto> findAllComments(final CommentReadCondition condition) {
        return commentRepository.findByBoardId(condition.getBoardId()).stream()
                .map(CommentDto::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentDto createComment(final CommentCreateRequest req, final Member member) {
        Board board = boardRepository.findById(req.getBoardId())
                .orElseThrow(BoardNotFoundException::new);

        Comment comment = new Comment(req.getContent(), member, board);
        commentRepository.save(comment);

        return CommentDto.toDto(comment);
    }

    @Transactional
    public void deleteComment(final Long id, final Member member) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(CommentNotFoundException::new);

        validateDeleteComment(comment, member);
        commentRepository.delete(comment);
    }

    private void validateDeleteComment(final Comment comment, final Member member) {
        if (!comment.isOwnComment(member)) {
            throw new MemberNotEqualsException();
        }
    }
}
