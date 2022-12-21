package yoon.community.repository.report;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import yoon.community.entity.report.BoardReportHistory;

import java.util.List;

public interface BoardReportRepository extends JpaRepository<BoardReportHistory, Integer> {
    Optional<BoardReportHistory> findByReporterIdAndReportedBoardId(int reporterId, int reportedBoardId);

    boolean existsByReporterIdAndReportedBoardId(int reporterId, int reportedBoardId);

    List<BoardReportHistory> findByReportedBoardId(int reportedBoardId);

    void deleteAllByReportedBoardId(int id);
}
