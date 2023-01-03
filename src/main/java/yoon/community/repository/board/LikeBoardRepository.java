package yoon.community.repository.board;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import yoon.community.entity.board.Board;
import yoon.community.entity.board.LikeBoard;
import yoon.community.entity.member.Member;

public interface LikeBoardRepository extends JpaRepository<LikeBoard, Long> {

    Optional<LikeBoard> findByBoardAndMember(Board board, Member member);
}
