package yoon.community.repository.board;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import yoon.community.entity.board.Board;
import yoon.community.entity.board.Favorite;
import yoon.community.entity.member.Member;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findFavoriteByBoard(Board board);
    Optional<Favorite> findByBoardAndUser(Board board, Member member);
    List<Favorite> findAllByUser(Member member);
}
