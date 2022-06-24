package yoon.community.repository.commnet;

import org.springframework.data.jpa.repository.JpaRepository;
import yoon.community.entity.comment.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByBoardId(int boardId);
}
