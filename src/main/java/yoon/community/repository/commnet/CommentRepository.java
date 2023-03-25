package yoon.community.repository.commnet;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import yoon.community.domain.comment.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByBoardId(Long boardId);
}
