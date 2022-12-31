package yoon.community.repository.board;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import yoon.community.entity.board.Board;
import yoon.community.entity.board.Favorite;
import yoon.community.entity.user.User;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findFavoriteByBoard(Board board);
    Optional<Favorite> findByBoardAndUser(Board board, User user);
    List<Favorite> findAllByUser(User user);
}
