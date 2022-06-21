package yoon.community.repository.board;

import org.springframework.data.jpa.repository.JpaRepository;
import yoon.community.entity.board.Board;

public interface BoardRepository extends JpaRepository<Board, Integer> {
}
