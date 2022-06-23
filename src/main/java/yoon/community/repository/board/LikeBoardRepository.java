package yoon.community.repository.board;

import org.springframework.data.jpa.repository.JpaRepository;
import yoon.community.entity.board.Board;
import yoon.community.entity.board.LikeBoard;
import yoon.community.entity.user.User;

public interface LikeBoardRepository extends JpaRepository<LikeBoard, Integer> {

    LikeBoard findByBoardAndUser(Board board, User user);
}
