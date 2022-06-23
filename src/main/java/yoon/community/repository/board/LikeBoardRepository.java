package yoon.community.repository.board;

import org.springframework.data.jpa.repository.JpaRepository;
import yoon.community.entity.board.LikeBoard;
import yoon.community.entity.user.User;

import java.util.Optional;

public interface LikeBoardRepository extends JpaRepository<LikeBoard, Integer> {

    LikeBoard findLikeBoardByUser(User user);
}
