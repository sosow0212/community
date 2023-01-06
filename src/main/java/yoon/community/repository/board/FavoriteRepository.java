package yoon.community.repository.board;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import yoon.community.domain.board.Board;
import yoon.community.domain.board.Favorite;
import yoon.community.domain.member.Member;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findFavoriteByBoard(Board board);
    Optional<Favorite> findByBoardAndMember(Board board, Member member);
    List<Favorite> findAllByMember(Member member);
}
