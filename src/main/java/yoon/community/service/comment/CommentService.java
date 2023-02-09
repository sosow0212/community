package yoon.community.service.comment;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@Service
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public List<CommentDto> findAllComments(CommentReadCondition condition) {
        List<Comment> comments = commentRepository.findByBoardId(condition.getBoardId());
        return comments.stream()
                .map(comment -> new CommentDto().toDto(comment))
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentDto createComment(CommentCreateRequest req, Member member) {
        Board board = boardRepository.findById(req.getBoardId()).orElseThrow(BoardNotFoundException::new);
        Comment comment = new Comment(req.getContent(), member, board);
        commentRepository.save(comment);
        return new CommentDto().toDto(comment);
    }

    @Transactional
    public void deleteComment(Long id, Member member) {
        Comment comment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        validateDeleteComment(comment, member);
        commentRepository.delete(comment);
    }

    private void validateDeleteComment(Comment comment, Member member) {
        if (!comment.isOwnComment(member)) {
            throw new MemberNotEqualsException();
        }
    }
}
