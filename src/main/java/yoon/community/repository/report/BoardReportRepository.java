package yoon.community.repository.report;

import org.springframework.data.jpa.repository.JpaRepository;
import yoon.community.entity.board.Board;
import yoon.community.entity.report.BoardReport;

import java.util.List;

public interface BoardReportRepository extends JpaRepository<BoardReport, Integer> {
    BoardReport findByReporterIdAndReportedBoardId(int reporterId, int reportedBoardId);
    List<BoardReport> findByReportedBoardId(int reportedBoardId);
}
