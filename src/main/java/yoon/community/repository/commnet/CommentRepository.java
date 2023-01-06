package yoon.community.repository.commnet;

import org.springframework.data.jpa.repository.JpaRepository;
import yoon.community.domain.comment.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByBoardId(Long boardId);
}
