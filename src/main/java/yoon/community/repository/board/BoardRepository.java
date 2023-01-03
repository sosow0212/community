package yoon.community.repository.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import yoon.community.entity.board.Board;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findByTitleContaining(String keyword, Pageable pageable);
    Page<Board> findAll(Pageable pageable);
    Page<Board> findAllByCategoryId(Pageable pageable, int categoryId);
    Page<Board> findByLikedGreaterThanEqual(Pageable pageable, int number);
    List<Board> findByReportedIsTrue();
}
