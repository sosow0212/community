package yoon.community.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoon.community.dto.board.BoardSimpleDto;
import yoon.community.dto.comment.CommentCreateRequest;
import yoon.community.dto.comment.CommentDto;
import yoon.community.dto.comment.CommentReadCondition;
import yoon.community.entity.board.Board;
import yoon.community.entity.comment.Comment;
import yoon.community.entity.user.User;
import yoon.community.exception.BoardNotFoundException;
import yoon.community.exception.CommentNotFoundException;
import yoon.community.exception.MemberNotEqualsException;
import yoon.community.exception.MemberNotFoundException;
import yoon.community.repository.board.BoardRepository;
import yoon.community.repository.commnet.CommentRepository;
import yoon.community.repository.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public List<CommentDto> findAll(CommentReadCondition condition) {
        List<Comment> commentList = commentRepository.findByBoardId(condition.getBoardId());
        List<CommentDto> commentDtoList = new ArrayList<>();
        commentList.stream().forEach(i -> commentDtoList.add(new CommentDto().toDto(i)));
        return commentDtoList;
    }

    @Transactional
    public CommentDto create(CommentCreateRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        Board board = boardRepository.findById(req.getBoardId()).orElseThrow(BoardNotFoundException::new);

        Comment comment = new Comment(req.getContent(), user, board);
        commentRepository.save(comment);
        return new CommentDto().toDto(comment);
    }

    @Transactional
    public void delete(int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        Comment comment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        Board board = boardRepository.findById(comment.getBoard().getId()).orElseThrow(BoardNotFoundException::new);
        if(comment.getUser().equals(user)) {
            // 삭제 진행
            commentRepository.delete(comment);
        } else {
            throw new MemberNotEqualsException();
        }
    }


}
