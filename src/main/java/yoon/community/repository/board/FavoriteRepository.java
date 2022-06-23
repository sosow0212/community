package yoon.community.repository.board;

import org.springframework.data.jpa.repository.JpaRepository;
import yoon.community.entity.board.Board;
import yoon.community.entity.board.Favorite;
import yoon.community.entity.user.User;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    Favorite findFavoriteByBoard(Board board);
    Favorite findByBoardAndUser(Board board, User user);
    List<Favorite> findAllByUser(User user);
}
